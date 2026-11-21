package com.ijioio.halcyon.processor.api;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;

public class ParseContext {

	public static ParseContext empty() {
		return new ParseContext(null, null);
	}

	public static ParseContext of(Element element) {
		return new ParseContext(element, null);
	}

	public static ParseContext of(Element element, AnnotationMirror annotationMirror) {
		return new ParseContext(element, annotationMirror);
	}

	private final Element element;

	private final AnnotationMirror annotationMirror;

	private ParseContext(Element element, AnnotationMirror annotationMirror) {

		this.element = element;
		this.annotationMirror = annotationMirror;
	}

	public Element getElement() {
		return element;
	}

	public AnnotationMirror getAnnotationMirror() {
		return annotationMirror;
	}

	public ParseContext withElement(Element element) {
		return new ParseContext(element, annotationMirror);
	}

	public ParseContext withAnnotationMirror(AnnotationMirror annotationMirror) {
		return new ParseContext(element, annotationMirror);
	}
}
