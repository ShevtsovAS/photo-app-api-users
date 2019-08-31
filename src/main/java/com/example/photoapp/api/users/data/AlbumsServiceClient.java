package com.example.photoapp.api.users.data;

import com.example.photoapp.api.users.ui.model.AlbumResponse;
import feign.FeignException;
import feign.hystrix.FallbackFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;

@FeignClient(name = "albums-ws", fallbackFactory = AlbumsFallbackFactory.class)
public interface AlbumsServiceClient {
    @GetMapping("/users/{id}/albums")
    List<AlbumResponse> getAlbums(@PathVariable String id);
}

@Component
class AlbumsFallbackFactory implements FallbackFactory<AlbumsServiceClient> {

    @Override
    public AlbumsServiceClient create(Throwable cause) {
        return new AlbumsServiceClientFallback(cause);
    }
}

@Slf4j
@RequiredArgsConstructor
class AlbumsServiceClientFallback implements AlbumsServiceClient {

    private final Throwable cause;

    @Override
    public List<AlbumResponse> getAlbums(String id) {
        if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
            log.error("404 error took place when getAlbums was called with userId: {}. Error message: {}", id, cause.getLocalizedMessage());
        } else {
            log.error("Other error took place: {}", cause.getLocalizedMessage());
        }
        return Collections.emptyList();
    }
}
