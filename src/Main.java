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
        createFileContents(new File(args[0]), fileContents);
        // キーワードリストを作成する
        List<String> keywords = createKeywords(new File(args[1]));
        // CSVに出力する
        outputCsv(fileContents, keywords);
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
    private static void outputCsv(Map<String, String> fileContents, List<String> keywords) throws IOException {
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

        fileContents.forEach((path, content) -> {
            for (String keyword: keywords) {
                if (content.contains(keyword)) {
                    pw.println();
                    // 項番
                    pw.print(counter.get());
                    pw.print(",");
                    // キーワード
                    pw.print(keyword);
                    pw.print(",");
                    // ファイルパス
                    pw.print(path);
                    counter.getAndIncrement();
                    break;
                }
            }
        });

        // ファイルを閉じる
        pw.close();

        System.out.println("出力完了");
    }
}
