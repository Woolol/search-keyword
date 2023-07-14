import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Sy Lian
 */
public class Main {
    static final String CSV_FILE_NAME = "Result.csv";

    public static void main(String[] args) throws IOException {
        Map<String, String> fileContents = new HashMap<>();
        // ファイル内容を取得する
        Timer timer = new Timer();
        timer.start();
        createFileContents(new File(args[0]), fileContents);
        timer.stop("ファイル内容を取得する");
        timer.reset();
        // キーワードリストを作成する
        timer.start();
        List<String> keywords = createKeywords(new File(args[1]));
        timer.stop("キーワードリストを作成する");
        timer.reset();
        // CSVに出力する
        timer.start();
        csvOutput(fileContents, keywords);
        timer.stop("CSVに出力する");
    }

    /**
     * ファイル内容を取得する
     *
     * @param rootPath     ファイルパス
     * @param fileContents ファイル内容Map
     * @throws IOException Exception
     */
    private static void createFileContents(File rootPath, Map<String, String> fileContents) throws IOException {
        File[] files = rootPath.listFiles();
        if (files == null) {
            return;
        }

        for (File f : files) {
            if (f.isDirectory()) {
                createFileContents(f, fileContents);
            }
            // 「.vue」、「.js」だけを対象とする
            if (f.getName().contains(".vue") || f.getName().contains(".js")) {
                fileContents.put(f.getPath(), Files.readString(Path.of(f.getPath())));
            }
        }
    }

    /**
     * キーワードリストを作成する
     *
     * @param file ファイルパス
     * @return キーワードリスト
     * @throws IOException Exception
     */
    private static List<String> createKeywords(File file) throws IOException {
        // TODO
        return new ArrayList<>();
    }

    /**
     * CSVに出力する
     *
     * @param fileContents ファイル内容Map
     * @param keywords     キーワードリスト
     * @throws IOException Exception
     */
    private static void csvOutput(Map<String, String> fileContents, List<String> keywords) throws IOException {
        AtomicInteger counter = new AtomicInteger(1);

        // 出力ファイルを作成
        FileWriter fw = new FileWriter(CSV_FILE_NAME, false);
        PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

        // ヘッダー指定
        pw.print("#");
        pw.print(",");
        pw.print("キーワード");
        pw.print(",");
        pw.print("ファイル");

        keywords.forEach(keyword -> {
            String filePath = "なし";

            for (String file : fileContents.keySet()) {
                if (fileContents.get(file).contains(keyword)) {
                    filePath = file;
                    break;
                }
            }
            pw.println();
            // 項番
            pw.print(counter.get());
            pw.print(",");
            // キーワード
            pw.print(keyword);
            pw.print(",");
            // ファイルパス
            pw.print(filePath);
            counter.getAndIncrement();
        });

        // ファイルを閉じる
        pw.close();

        System.out.println("出力完了");
    }

    /**
     * タイマークラス
     */
    static class Timer {
        long start;

        public void start() {
            start = System.currentTimeMillis();
        }

        public void stop(String label) {
            long now = System.currentTimeMillis();
            System.out.println("【" + label + "】所要時間：" + (now - start) / 1000.0 + "秒");
        }

        public void reset() {
            start = 0;
        }
    }
}
