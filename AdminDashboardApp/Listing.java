public class Listing {
  private String id;
  private String title;
  private String description;
  private String status;
  private String sellerId;
  private String duration;

  public Listing(String id, String title, String description, String status, String sellerId, String duration) {
      this.id = id;
      this.title = title;
      this.description = description;
      this.status = status;
      this.sellerId = sellerId;
      this.duration = duration;
  }

  public String getId() {
      return id;
  }

  public String getTitle() {
      return title;
  }

  public String getDescription() {
      return description;
  }

  public String getStatus() {
      return status;
  }

  public String getSellerId() {
      return sellerId;
  }

  public String getDuration() {
      return duration;
  }
}
