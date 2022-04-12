package chart.desk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum AssetKind {
    /**
     * Helm index.
     */
    HELM_INDEX(".yaml"),
    /**
     * Signature helm files
     */
    HELM_PROVENANCE(".tgz.prov"),
    /**
     * Helm package
     */
    HELM_PACKAGE(".tgz");

    @Getter
    private final String extension;
}
