package chart.desk.services.storage;

import chart.desk.model.AssetKind;

/**
 * Services that save charts in different systems.
 */
public interface StorageService {

    String save(byte[] file, String name, String version, AssetKind assetKind, String userId);

    byte[] get(String name, String version, AssetKind assetKind, String userId);

    StorageType type();
}
