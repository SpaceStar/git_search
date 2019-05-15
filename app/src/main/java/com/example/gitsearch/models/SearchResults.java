package com.example.gitsearch.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResults {
    @SerializedName("total_count")
    private long totalCount;
    private List<Item> items;

    public long getTotalCount() {
        return totalCount;
    }

    public List<Item> getItems() {
        return items;
    }

    public static class Item implements Parcelable {
        private String name;
        private Owner owner;

        public Item(Parcel source) {
            name = source.readString();
            owner = new Owner();
            owner.login = source.readString();
            owner.avatarUrl = source.readString();
        }

        public String getName() {
            return name;
        }

        public Owner getOwner() {
            return owner;
        }

        public static class Owner {
            private String login;
            @SerializedName("avatar_url")
            private String avatarUrl;

            public String getLogin() {
                return login;
            }

            public String getAvatarUrl() {
                return avatarUrl;
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeString(owner.login);
            dest.writeString(owner.avatarUrl);
        }

        public static final Creator<Item> CREATOR = new Creator<Item>() {
            @Override
            public Item createFromParcel(Parcel source) {
                return new Item(source);
            }

            @Override
            public Item[] newArray(int size) {
                return new Item[size];
            }
        };
    }
}