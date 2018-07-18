package com.agileengine.finders;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SimpleElementFinder implements ElementFinder {

    /**
     * Indicates the threshold of the weight which should have the element to be considered
     * into finding algorithm. Increase this value for more stringent elements filtering
     */
    private static final int WEIGHT_SUCCESS_THRESHOLD = 40;
    private static final int DEFAULT_ATTRIBUTE_WEIGHT = 2;
    private static final String CSS_ALL_SELECTOR = "*";

    private static final Map<String, Integer> ATTRIBUTES_WEIGHTS = new HashMap<String, Integer>() {{
        put("id", 100);
        put("title", 50);
        put("text", 40);
        put("class", 25);
        put("href", 5);
        put("onclick", 5);
    }};

    private Document document;
    private Element targetElement;

    public SimpleElementFinder(Document document, Element targetElement) {
        this.document = document;
        this.targetElement = targetElement;
    }

    @Override
    public Optional<Element> findMostSimilarElement() {
        Elements elements = document.select(CSS_ALL_SELECTOR);

        Element mostSimilarElement = null;
        int mostSimilarElementWeight = 0;

        for (Element element : elements) {
            int elementWeight = getElementWeight(element);

            if (elementWeight > mostSimilarElementWeight) {
                mostSimilarElementWeight = elementWeight;

                if (mostSimilarElementWeight > WEIGHT_SUCCESS_THRESHOLD) {
                    mostSimilarElement = element;
                }
            }
        }

        return Optional.ofNullable(mostSimilarElement);
    }

    private int getElementWeight(Element element) {
        Attributes elementAttributes = element.attributes();
        Attributes requiredAttributes = targetElement.attributes();

        int totalWeight = 0;
        for (Attribute attribute : elementAttributes) {
            String attributeKey = attribute.getKey();
            String requiredAttributeValue = requiredAttributes.get(attributeKey);

            if (StringUtils.isNotEmpty(requiredAttributeValue)
                    && requiredAttributeValue.equalsIgnoreCase(attribute.getValue())) {
                totalWeight += ATTRIBUTES_WEIGHTS.getOrDefault(attributeKey, DEFAULT_ATTRIBUTE_WEIGHT);
            }
        }

        if (targetElement.text().equalsIgnoreCase(element.text())) {
            totalWeight += ATTRIBUTES_WEIGHTS.getOrDefault("text", DEFAULT_ATTRIBUTE_WEIGHT);
        }

        return totalWeight;
    }

    @Override
    public String findElementPath(Element element) {
        StringBuilder pathBuilder = new StringBuilder();
        Elements parents = element.parents();

        for (int i = parents.size() - 1; i >= 0; i--) {
            Element currentElement = parents.get(i);
            appendElementIdentifierToPath(currentElement, pathBuilder);
        }

        appendElementIdentifierToPath(element, pathBuilder);
        return pathBuilder.toString();
    }

    private void appendElementIdentifierToPath(Element element, StringBuilder pathBuilder) {
        pathBuilder.append(PATH_DELIMITER);
        pathBuilder.append(element.tagName());
        pathBuilder.append("[");
        pathBuilder.append(element.elementSiblingIndex());
        pathBuilder.append("]");
    }
}
