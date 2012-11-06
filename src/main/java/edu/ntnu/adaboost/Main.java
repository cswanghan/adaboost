package edu.ntnu.adaboost;

import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.ntnu.adaboost.controller.AppController;
import edu.ntnu.adaboost.dependencyinjection.AdaboostModule;
import org.apache.commons.cli.*;

public class Main {

    public static void main(String[] args) {
        Options options = createArgsOptions();

        PosixParser parser = new PosixParser();

        try {
            CommandLine cmd = parser.parse(options, args);

            String filename = cmd.getOptionValue("f");
            String[] classifiersCount = cmd.getOptionValues("n");

            int nbcCount = 0;
            int dtcCount = 0;
            if (classifiersCount.length == 2) {
                nbcCount = Integer.parseInt(classifiersCount[0]);
                dtcCount = Integer.parseInt(classifiersCount[1]);
            } else {
                throw new IllegalArgumentException("Invalid number of NBCs or DTCs");
            }

            double trainTestRatio = Double.parseDouble(cmd.getOptionValue("p")) / 100.0d;

            Injector injector = Guice.createInjector(new AdaboostModule());
            AppController learningController = injector.getInstance(AppController.class);
            learningController.start(filename, trainTestRatio, nbcCount, dtcCount);
        } catch (ParseException e) {
            e.printStackTrace();
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("Adaboost", options);
        }
    }

    private static Options createArgsOptions() {
        Options options = new Options();
        Option filename = OptionBuilder.withArgName("FILENAME").hasArg().withDescription("The name of the data file")
                .isRequired().create("f");
        Option classifierCount = OptionBuilder.withArgName("NBC DTC").hasArgs(2).withDescription("Number of NBC and " +
                "DTC")
                .isRequired().create("n");
        Option percentageTraining = OptionBuilder.withArgName("PERCENTAGE").hasArg().withDescription("The percentage " +
                "of the data set to be used for training(ex: 80)").isRequired().create("p");

        options.addOption(filename);
        options.addOption(classifierCount);
        options.addOption(percentageTraining);

        return options;
    }

}