package software.amazon.smithy.ts.codegen;

import software.amazon.smithy.build.FileManifest;
import software.amazon.smithy.codegen.core.SymbolProvider;
import software.amazon.smithy.codegen.core.WriterDelegator;

/**
 * TypeScriptWriterDelegator is a container that manages the use of multiple TypeScriptWriters, each for an individual
 * generated source file.
 * - We call delegator.useFileWriter() to "check out" a writer for a specific file.
 * - When we're done generating code, we call delegator.flushWriters() to write all of our files to the smithy build's
 *   output directory.
 */
public class TypeScriptWriterDelegator extends WriterDelegator<TypeScriptWriter> {
    public TypeScriptWriterDelegator(FileManifest fileManifest, SymbolProvider symbolProvider) {
        super(fileManifest, symbolProvider, (file, namespace) -> new TypeScriptWriter());
    }
}
