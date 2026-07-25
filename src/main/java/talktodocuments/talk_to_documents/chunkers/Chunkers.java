package talktodocuments.talk_to_documents.chunkers;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class Chunkers {
    private final Map<String, Chunker> fileFormatChunkers = new ConcurrentHashMap<>();

    private Chunker fallbackChunker;

    public void registerChunker(String format, Chunker chunker) {
        fileFormatChunkers.put(format, chunker);
    }

    public void registerFallbackChunker(Chunker chunker) {
        fallbackChunker = chunker;
    }

    public void deregisterChunker(String format) {
        fileFormatChunkers.remove(format);
    }

    public void deregisterFallbackChunker() {
        fallbackChunker = null;
    }

    public List<String> getChunks(File input, String fileName) throws Exception {
        String extension;
        int extensionIndex = fileName.lastIndexOf('.');
        if (extensionIndex == -1) {
            extension = "";
        } else {
            extension = fileName.substring(extensionIndex + 1);
        }
        Chunker chunker = fileFormatChunkers.get(extension);
        if (chunker == null) {
            chunker = fallbackChunker;
        }
        if (chunker == null) {
            throw new IllegalArgumentException("Chunking of file with extension [" + extension + "] is not supported.");
        }
        return chunker.getChunks(input);
    }
}
