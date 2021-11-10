package org.perekladov;

import org.perekladov.site.Parser;
import org.perekladov.view.ConsoleView;


public class Runner {

    public static void main(String[] args) {
        while (true) {
            ConsoleView view = new ConsoleView();
            Parser parser = view.getParser();
            view.runParser(parser);
        }
    }
}
