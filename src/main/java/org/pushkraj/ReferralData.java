package org.pushkraj;

import java.util.HashMap;
import java.util.Map;

public class ReferralData {
    private Map<String, PlayerReferralData> referralData;

    public ReferralData() {
        this.referralData = new HashMap<>();
    }

    public static class PlayerReferralData {
        private int referralCount;
        private Map<String, Long> referredPlayers;
        private long lastReferralTime;

        public PlayerReferralData() {
            this.referralCount = 0;
            this.referredPlayers = new HashMap<>();
            this.lastReferralTime = 0;
        }

        public int getReferralCount() {
            return referralCount;
        }

        public void incrementReferralCount() {
            this.referralCount++;
        }

        public Map<String, Long> getReferredPlayers() {
            return referredPlayers;
        }

        public long getLastReferralTime() {
            return lastReferralTime;
        }

        public void setLastReferralTime(long time) {
            this.lastReferralTime = time;
        }

        public boolean hasReferred(String playerName) {
            return referredPlayers.containsKey(playerName);
        }

        public void addReferral(String playerName, long time) {
            referredPlayers.put(playerName, time);
            incrementReferralCount();
            setLastReferralTime(time);
        }
    }

    public PlayerReferralData getPlayerData(String playerName) {
        return referralData.computeIfAbsent(playerName, k -> new PlayerReferralData());
    }

    public boolean hasReferral(String referrer, String referred) {
        PlayerReferralData data = referralData.get(referrer);
        return data != null && data.hasReferred(referred);
    }

    public void addReferral(String referrer, String referred) {
        getPlayerData(referrer).addReferral(referred, System.currentTimeMillis());
    }
}