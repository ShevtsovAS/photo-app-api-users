package com.example.photoapp.api.users.data;

import com.example.photoapp.api.users.ui.model.AlbumResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "albums-ws")
public interface AlbumsServiceClient {
    @GetMapping("/users/{id}/albums")
    List<AlbumResponse> getAlbums(@PathVariable String id);
}