package dk.easv.cs5.mytunes.bll.tools;

public class FormattingTool {
    public static String format(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds); //
    }

}
