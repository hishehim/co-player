package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.Constraints;
import statics.Domain;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.io.UnsupportedEncodingException;

/**
 * Created by Mike on 3/6/2016.
 * Entity for holding information on each playlists item
 */
@Table(
        uniqueConstraints =
                @UniqueConstraint(columnNames = {"parent__id","_id"})
)
@Entity
public class PlaylistItem extends Model {

    @Id
    @GeneratedValue
    @Column(name = "_id")
    @JsonIgnore
    private long rowId;

    @Constraints.Required
    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "parent__id")
    @JsonIgnore
    private Playlist parent;

    /**
     * Identifies the player source
     * */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @Constraints.Required
    @Constraints.MaxLength(40)
    @Column(name = "source_type")
    private String sourceType;

    @Constraints.Required
    @Column(name = "identifier")
    private String link;

    private String thumbnailUrl;

    @Column(name = "author")
    private String srcAuthor;

    @Column(name = "title")
    private String srcTitle;
    /**
     * used prevent initialization without the use of factory method
     * Also used to return a null-pattern object
     */
    private PlaylistItem() {}

    public static Finder<Long, PlaylistItem> find = new Finder<Long, PlaylistItem>(PlaylistItem.class);


    /**
     * Factory method for creating a playlists item. Data are validated here.
     * @param link the embedded video link
     * @param parent the playlist the new item shall belong to
     * @param srcDomain the original source of the link
     * */
    public static PlaylistItem getNewItem(@Nonnull String link,
                                          @Nonnull Playlist parent,
                                          @Nonnull Domain srcDomain) {

        PlaylistItem nPlaylistItem = new PlaylistItem();
        nPlaylistItem.link = link;
        nPlaylistItem.parent = parent;
        nPlaylistItem.sourceType = srcDomain.toString();
        return nPlaylistItem;
    }

    public static PlaylistItem getNewItem(@Nonnull String link,
                                          @Nonnull Playlist parent,
                                          @Nonnull Domain srcDomain,
                                          String title,
                                          String author,
                                          String thumbnail) {

        PlaylistItem nPlaylistItem = getNewItem(link, parent, srcDomain);
        nPlaylistItem.thumbnailUrl = thumbnail;
        nPlaylistItem.srcAuthor = author;
        nPlaylistItem.srcTitle = title;
        return nPlaylistItem;
    }


    @JsonIgnore
    public long getId() {
        return rowId;
    }

    @JsonIgnore
    public Playlist getParent() {
        return parent;
    }

    public String getSourceType() {
        return sourceType;
    }

    public String getLink() {
        return link;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getTitle() {
        try {
            return new String(srcTitle.getBytes("UTF8"), "UTF8");
        } catch (UnsupportedEncodingException e) {
            return srcTitle;
        }
    }

    public String getAuthor() {
        return srcAuthor;
    }

}