package pl.mjurek.notepage.service;

public class NoteActionService {
    public static String convertReadableNewLine(String text) {
        return text.replaceAll("<br/>", "&#013;&#010;");
    }

    public static String convertNewLineCharToBR_Tag(String text) {
        return text.replaceAll("(\r\n|\n)", "<br/>");
    }
}
