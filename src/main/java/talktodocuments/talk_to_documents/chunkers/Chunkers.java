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

    public List<String> getChunks(File input, String extension) throws Exception {
        if (extension == null) {
            String fileName = input.getName();
            int extensionIndex = fileName.lastIndexOf('.');
            if (extensionIndex == -1) {
                extension = "";
            } else {
                extension = fileName.substring(extensionIndex + 1);
            }
        }
        Chunker chunker = fileFormatChunkers.get(extension);
        if (chunker == null) {
            throw new IllegalArgumentException("Chunking of file with extension [" + extension+"] is not supported.");
        }
        return chunker.getChunks(input);
    }
}
