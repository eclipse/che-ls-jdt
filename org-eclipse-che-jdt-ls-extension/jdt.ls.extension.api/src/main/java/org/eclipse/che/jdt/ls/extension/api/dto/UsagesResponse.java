package org.eclipse.che.jdt.ls.extension.api.dto;

import java.util.List;
import org.eclipse.lsp4j.SymbolKind;

public class UsagesResponse {
  private String searchedElement;
  private SymbolKind elementKind;
  private List<SearchResult> searchResults;

  public UsagesResponse() {}

  public UsagesResponse(
      String searchedElement, SymbolKind elementKind, List<SearchResult> searchResults) {
    this.searchedElement = searchedElement;
    this.elementKind = elementKind;
    this.searchResults = searchResults;
  }

  public String getSearchedElement() {
    return searchedElement;
  }

  public void setSearchedElement(String searchedElement) {
    this.searchedElement = searchedElement;
  }

  public SymbolKind getElementKind() {
    return elementKind;
  }

  public void setElementKind(SymbolKind elementKind) {
    this.elementKind = elementKind;
  }

  public List<SearchResult> getSearchResults() {
    return searchResults;
  }

  public void setSearchResults(List<SearchResult> searchResults) {
    this.searchResults = searchResults;
  }
}
