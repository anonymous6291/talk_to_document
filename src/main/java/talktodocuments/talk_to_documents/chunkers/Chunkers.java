package talktodocuments.talk_to_documents.chunkers;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class Chunkers {
    private final Map<String, Chunker> fileFormatChunkers = new ConcurrentHashMap<>();

    public void registerChunker(String format, Chunker chunker) {
        fileFormatChunkers.put(format, chunker);
    }

    public void deregisterChunker(String format) {
        fileFormatChunkers.remove(format);
    }

    public List<String> getChunks(File input) throws Exception {
        String fileName = input.getName();
        int extensionIndex = fileName.lastIndexOf('.');
        Chunker chunker;
        if (extensionIndex == -1) {
            chunker = fileFormatChunkers.get("");
        } else {
            chunker = fileFormatChunkers.get(fileName.substring(extensionIndex + 1));
        }
        return chunker.getChunks(input);
    }
}
