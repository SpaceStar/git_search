package com.example.gitsearch.models;

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

    public class Item {
        private String name;
        private Owner owner;

        public String getName() {
            return name;
        }

        public Owner getOwner() {
            return owner;
        }

        public class Owner {
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
    }
}