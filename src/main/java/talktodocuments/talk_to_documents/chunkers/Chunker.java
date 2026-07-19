package talktodocuments.talk_to_documents.chunkers;

import java.io.File;
import java.util.List;

public interface Chunker {
    List<String> getChunks(File input) throws Exception;
}
