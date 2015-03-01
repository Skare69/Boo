package com.github.skare69.boo;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Simple service to recursively scan a directory for files, create a MD5 hash per file and store it in a hashmap.
 * <p/>
 * User: Dominik
 * Date: 24.02.2015
 * Time: 09:52
 */
public class DuplicateFileFinder
{
    private static Logger logger = Logger.getLogger(DuplicateFileFinder.class);
    /**
     * Max file size default value: 5 MB
     */
    private static final Long DEFAULT_MAX_FILE_SIZE = 5242880l;

    private static Options options;

    /**
     * Set the CLI options.
     */
    static
    {
        options = new Options();
        for (CliOption option : CliOption.values())
        {
            Option newOption = new Option(option.getOpt(), option.getHasArg(), option.getDescription());
            if (option.getLongOpt() != null)
            {
                newOption.setLongOpt(option.getLongOpt());
            }
            newOption.setRequired(option.isRequired());

            options.addOption(newOption);
        }
    }
    private HashMap<String, List<String>> fileHashesMap = new HashMap<>();
    private Integer fileCount = 0;
    private CommandLine commandLine;

    public DuplicateFileFinder(String[] args)
    {
        if (args == null)
            return;

        CommandLineParser parser = new BasicParser();
        try
        {
            setCommandLine(parser.parse(options, args));

            if (getCommandLine().hasOption(CliOption.HELP.getOpt()))
            {
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("boo", options);
                return;
            }
        }
        catch (ParseException e)
        {
            logger.info(e.getMessage());
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("boo", options);
            return;
        }

        if (getCommandLine().hasOption(CliOption.VERBOSE_OUTPUT.getOpt()))
        {
            LogManager.getRootLogger().setLevel(Level.DEBUG);
        }
        if (getCommandLine().hasOption(CliOption.FLAT_SCAN.getOpt()))
        {
            logger.info("Doing a flat scan; i.e. not recursively scanning directories. ");
        }
        if (getCommandLine().hasOption(CliOption.HIDDEN_FILES.getOpt()))
        {
            logger.info("Including hidden files in scan.");
        }
    }

    public static void main(String[] args)
    {
        DuplicateFileFinder duplicateFileFinder = new DuplicateFileFinder(args);

        duplicateFileFinder.scanDirectory();
        displayResults(duplicateFileFinder);
    }

    public Map<String, List<String>> processScannedFileMap()
    {
        Iterator<Map.Entry<String, List<String>>> iterator = getFileHashesMap().entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry<String, List<String>> entry = iterator.next();
            if (entry.getValue().size() < 2)
                iterator.remove();
        }
        return getFileHashesMap();
    }

    /**
     * Display the results of the scanned files. Lists all found duplicates if any were found.
     *
     * @param duplicateFileFinder    The {@link com.github.skare69.boo.DuplicateFileFinder} instance which was used to scan the files.
     */
    public static void displayResults(DuplicateFileFinder duplicateFileFinder)
    {
        duplicateFileFinder.processScannedFileMap();

        for (Map.Entry<String, List<String>> entry : duplicateFileFinder.getFileHashesMap().entrySet())
        {
            logger.info("Found duplicates");
            for (String file : entry.getValue())
            {
                logger.info("* " + file);
            }
            logger.info(""); // pseudo new line as the logger automatically inserts a line separator after each log message
        }

        if (duplicateFileFinder.getFileHashesMap().isEmpty())
        {
            logger.info("No duplicates found.");
        }
    }

    public CommandLine getCommandLine()
    {
        return commandLine;
    }

    public void setCommandLine(CommandLine commandLine)
    {
        this.commandLine = commandLine;
    }

    public Integer getFileCount()
    {
        return fileCount;
    }

    /**
     * Increment the {@code fileCount} by 1.
     */
    private void incrementFileCount()
    {
        fileCount++;
    }

    public HashMap<String, List<String>> getFileHashesMap()
    {
        return fileHashesMap;
    }

    /**
     * Add a new entry to the {@code fileHashesMap} if the file's MD5 hash is not yet contained in the list. Add it to the appropriate
     * entry's list otherwise.
     *
     * @param file    The new file to add.
     * @throws IOException if the file can't be hashed
     */
    private void addToFileHashesMap(File file) throws IOException
    {
        String md5Hex = DigestUtils.md5Hex(new FileInputStream(file));
        if (fileHashesMap.containsKey(md5Hex))
        {
            fileHashesMap.get(md5Hex).add(file.getAbsolutePath());
        }
        else
        {
            List<String> newFilePathList = new ArrayList<>(1);
            newFilePathList.add(file.getAbsolutePath());
            fileHashesMap.put(md5Hex, newFilePathList);
        }
    }

    /**
     * Recursively scan the provided files. If an entry is a file it is added to the {@code fileHashesMap}. If it is a directory it is
     * also scanned by this method.
     *
     * @param filesInDirectory    a list of files to scan
     */
    private void scanFilesInDirectory(File[] filesInDirectory)
    {
        if (filesInDirectory.length > 0)
        {
            for (File file : filesInDirectory)
            {
                if (file.isHidden())
                {
                    if (!commandLine.hasOption(CliOption.HIDDEN_FILES.getOpt()))
                        continue;
                }
                Long maxFileSize = DEFAULT_MAX_FILE_SIZE;
                String maxFileSizeOptionValue = getCommandLine().getOptionValue(CliOption.MAX_FILE_SIZE.getOpt());
                if (maxFileSizeOptionValue != null)
                {
                    maxFileSize = Long.valueOf(maxFileSizeOptionValue);
                }
                if (file.length() > maxFileSize)
                {
                    logger.debug(String.format("Skipping file %s due to file size (file: %d, max: %d)", file.getAbsolutePath(),
                            file.length(), maxFileSize));
                    continue;
                }
                if (file.isFile())
                {
                    logger.debug(String.format("Scanning file %s", file.getAbsolutePath()));
                    incrementFileCount();
                    try
                    {
                        addToFileHashesMap(file);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else if (file.isDirectory())
                {
                    if (getCommandLine().hasOption(CliOption.FLAT_SCAN.getOpt()))
                    {
                        return;
                    }
                    logger.debug(String.format("Scanning directory %s", file.getAbsolutePath()));
                    scanDirectory(file);
                }
            }
        }
    }

    /**
     * Scan the specified directory for file duplicates.
     *
     * @param directory    the directory to scan
     */
    private void scanDirectory(File directory)
    {
        File[] filesInDirectory = getFilesInDirectory(directory);
        scanFilesInDirectory(filesInDirectory);
    }

    public void scanDirectory()
    {
        scanDirectory(getCommandLine().getOptionValue(CliOption.DIRECTORY_TO_SCAN.getOpt()));
    }

    /**
     * Scan the specified directory for file duplicates.
     *
     * @param directoryPath    the directory to scan
     */
    public void scanDirectory(String directoryPath)
    {
        File[] filesInDirectory = getFilesInDirectory(directoryPath);
        logger.debug(String.format("Scanning files in directory: %s", directoryPath));
        scanFilesInDirectory(filesInDirectory);
        logger.info(String.format("Scan finished: %d files scanned.", getFileCount()));
    }

    /**
     * Alternative version of the {@link com.github.skare69.boo.DuplicateFileFinder#getFilesInDirectory(java.io.File)} method; taking a
     * {@link java.lang.String} as the input argument instead of a {@link java.io.File}.
     *
     * @param directoryPath    The path of the directory to scan.
     * @return a list of files contained in this directory (including files and other directories) if there were any; an empty array
     * otherwise
     */
    private File[] getFilesInDirectory(String directoryPath)
    {
        File directory = new File(directoryPath);
        return getFilesInDirectory(directory);
    }

    /**
     * Get all files in the specified directory.
     *
     * @param directory    the directory to scan.
     * @return a list of files contained in this directory (including files and other directories) if there were any; an empty array
     * otherwise
     */
    private File[] getFilesInDirectory(File directory)
    {
        if (directory.isDirectory())
            return directory.listFiles();
        else
            return new File[]{};
    }

    /**
     * Re-initialize this service. Read new {@code args} and reset fields to default values.
     *
     * @param args The new arguments to use.
     */
    public void reInitializeConfig(String[] args)
    {
        fileHashesMap = new HashMap<>();
        fileCount = 0;

        CommandLineParser parser = new BasicParser();
        try
        {
            setCommandLine(parser.parse(options, args));
        }
        catch (ParseException e)
        {
            logger.error(e);
        }
    }

    /**
     * This enum contains all commandline interface options for easier access.
     */
    private enum CliOption
    {
        HELP("h", "help", false, "display this help message"),
        HIDDEN_FILES("s", false, "include hidden files (default: false)"),
        DIRECTORY_TO_SCAN("t", "target", true, "target directory to scan", true),
        FLAT_SCAN("f", "flat", false, "do a flat scan of the provided directory only; i.e. no recursion (default: false)"),
        VERBOSE_OUTPUT("v", "verbose", false, "print a verbose output of what's happening (default: false)"),
        MAX_FILE_SIZE("m", "max", true, "the maximum size of a file to scan in bytes (default: 5 MB)");

        private String opt;
        private String longOpt;
        private boolean hasArg;
        private String description;
        private boolean required;

        CliOption(String opt, boolean hasArg, String description)
        {
            this.opt = opt;
            this.hasArg = hasArg;
            this.description = description;
        }

        CliOption(String opt, String longOpt, boolean hasArg, String description)
        {
            this.opt = opt;
            this.longOpt = longOpt;
            this.hasArg = hasArg;
            this.description = description;
        }

        CliOption(String opt, String longOpt, boolean hasArg, String description, boolean required)
        {
            this.opt = opt;
            this.longOpt = longOpt;
            this.hasArg = hasArg;
            this.description = description;
            this.required = required;
        }

        public String getOpt()
        {
            return opt;
        }

        public String getLongOpt()
        {
            return longOpt;
        }

        public boolean getHasArg()
        {
            return hasArg;
        }

        public String getDescription()
        {
            return description;
        }

        public boolean isRequired()
        {
            return required;
        }
    }
}
