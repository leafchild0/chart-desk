package chart.desk.util;

import com.google.common.io.ByteStreams;
import lombok.SneakyThrows;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.io.SequenceInputStream;

/**
 * Utils for file operations
 *
 * @author vmalyshev
 */
public final class FileUtils {

	public static Mono<byte[]> toByteArray(Flux<FilePart> fileParts) {
		return fileParts
			.flatMap(Part::content)
			.map(DataBuffer::asInputStream)
			.reduce(SequenceInputStream::new)
			.map(FileUtils::toByteArray);
	}

	@SneakyThrows
	private static byte[] toByteArray(InputStream inputStream) {
		return ByteStreams.toByteArray(inputStream);
	}
}
