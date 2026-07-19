package talktodocuments.talk_to_documents.chunkers;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

@Service
public class TextFileChunker implements Chunker {
    @Getter
    private final List<String> supportedFileFormats = List.of("txt", "log", "");
    private final TextChunker textChunker;

    public TextFileChunker(TextChunker textChunker, Chunkers chunkers) {
        this.textChunker = textChunker;
        for (String format : supportedFileFormats) {
            chunkers.registerChunker(format, this);
        }
    }

    @Override
    public List<String> getChunks(File input) throws Exception {
        try (BufferedReader bufferedReader = Files.newBufferedReader(input.toPath())) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return textChunker.getChunks(stringBuilder.toString());
        }
    }
}
