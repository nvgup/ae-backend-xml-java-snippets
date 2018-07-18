package com.agileengine.finders;

import org.jsoup.nodes.Element;

import java.util.Optional;

public interface ElementFinder {
    String PATH_DELIMITER = " > ";

    Optional<Element> findMostSimilarElement();

    String findElementPath(Element element);
}