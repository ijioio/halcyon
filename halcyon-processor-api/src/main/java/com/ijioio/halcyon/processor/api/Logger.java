package com.ijioio.halcyon.processor.api;

import java.util.Objects;

import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

public class Logger {

	public static Logger of(Messager messager, boolean verbose) {
		return new Logger(messager, verbose);
	}

	private final Messager messager;
	private final boolean verbose;

	private Logger(Messager messager, boolean verbose) {

		this.messager = messager;
		this.verbose = verbose;
	}

	public void debug(CharSequence message) {

		if (verbose) {
			print(Kind.NOTE, message, null);
		}
	}

	public void debug(CharSequence message, MessageContext context) {

		if (verbose) {
			print(Kind.NOTE, message, context);
		}
	}

	public void info(CharSequence message) {
		print(Kind.NOTE, message, null);
	}

	public void info(CharSequence message, MessageContext context) {
		print(Kind.NOTE, message, context);
	}

	public void warn(CharSequence message) {
		print(Kind.WARNING, message, null);
	}

	public void warn(CharSequence message, MessageContext context) {
		print(Kind.WARNING, message, context);
	}

	public void error(CharSequence message) {
		print(Kind.ERROR, message, null);
	}

	public void error(CharSequence message, MessageContext context) {
		print(Kind.ERROR, message, context);
	}

	public void print(Kind kind, CharSequence message, MessageContext context) {

		Objects.requireNonNull(kind);
		Objects.requireNonNull(message);

		messager.printMessage(kind, message, context != null ? context.getElement() : null,
				context != null ? context.getAnnotationMirror() : null,
				context != null ? context.getAnnotationValue() : null);
	}

	public static class MessageContext {

		public static MessageContext empty() {
			return new MessageContext(null, null, null);
		}

		public static MessageContext of(Element element) {
			return new MessageContext(element, null, null);
		}

		public static MessageContext of(Element element, AnnotationMirror annotationMirror) {
			return new MessageContext(element, annotationMirror, null);
		}

		public static MessageContext of(Element element, AnnotationMirror annotationMirror,
				AnnotationValue annotationValue) {
			return new MessageContext(element, annotationMirror, annotationValue);
		}

		private final Element element;

		private final AnnotationMirror annotationMirror;

		private final AnnotationValue annotationValue;

		private MessageContext(Element element, AnnotationMirror annotationMirror, AnnotationValue annotationValue) {

			this.element = element;
			this.annotationMirror = annotationMirror;
			this.annotationValue = annotationValue;
		}

		public Element getElement() {
			return element;
		}

		public AnnotationMirror getAnnotationMirror() {
			return annotationMirror;
		}

		public AnnotationValue getAnnotationValue() {
			return annotationValue;
		}

		public MessageContext withElement(Element element) {
			return new MessageContext(element, annotationMirror, annotationValue);
		}

		public MessageContext withAnnotationMirror(AnnotationMirror annotationMirror) {
			return new MessageContext(element, annotationMirror, annotationValue);
		}

		public MessageContext withAnnotationValue(AnnotationValue annotationValue) {
			return new MessageContext(element, annotationMirror, annotationValue);
		}
	}
}
