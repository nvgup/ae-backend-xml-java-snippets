package com.agileengine;

import com.agileengine.finders.ElementFinder;
import com.agileengine.finders.SimpleElementFinder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class App {

    private static Logger LOGGER = LoggerFactory.getLogger(App.class);

    private static final String CHARSET_NAME = "utf8";
    private static final String DEFAULT_TARGET_ELEMENT_ID = "make-everything-ok-button";

    public static void main(String[] args) {
        if (args.length < 2) {
            LOGGER.warn("You mast provide <input_origin_file_path> and <input_other_sample_file_path> via command line");
            return;
        }

        String originalHtmlFilePath = args[0];
        String otherSampleFilePath = args[1];
        String targetElementId = args.length > 2 ? args[2] : DEFAULT_TARGET_ELEMENT_ID;

        Optional<Document> originalDocument = readDocument(new File(originalHtmlFilePath));
        Optional<Element> targetElement = originalDocument.map(doc -> doc.getElementById(targetElementId));
        if (!targetElement.isPresent()) {
            LOGGER.warn("There is no element with the id={} into original document: {}", targetElementId, originalHtmlFilePath);
            return;
        }

        Optional<Document> otherSampleDocument = readDocument(new File(otherSampleFilePath));
        otherSampleDocument.ifPresent(doc -> {
            ElementFinder simpleElementFinder = new SimpleElementFinder(doc, targetElement.get());
            simpleElementFinder.findMostSimilarElement()
                    .map(simpleElementFinder::findElementPath)
                    .ifPresent(p -> LOGGER.info("The path of found element is: {}", p));
        });
    }

    private static Optional<Document> readDocument(File htmlFile) {
        try {
            Document doc = Jsoup.parse(
                    htmlFile,
                    CHARSET_NAME,
                    htmlFile.getAbsolutePath());

            return Optional.of(doc);
        } catch (IOException e) {
            LOGGER.error(String.format("Error reading [%s] file", htmlFile.getAbsolutePath()), e);
            return Optional.empty();
        }
    }

}