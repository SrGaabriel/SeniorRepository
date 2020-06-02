package com.raidplugin.sdk.repository.member;

import com.raidplugin.api.prototype.Team;
import com.raidplugin.api.prototype.member.Member;
import com.raidplugin.api.repository.Repository;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MemberRepository implements Repository<UUID, Member> {

    /**
     * That's a member repository.
     * You can get member with:
     *  Team;
     *  Player;
     *  Team and Player.
     */

    private static MemberRepository memberRepository;

    public static MemberRepository getInstance() {
        return memberRepository == null ? (memberRepository = new MemberRepository()) : memberRepository;
    }

    private final Map<UUID, Member>  memberMap = new LinkedHashMap<>();

    @Override
    public Map<UUID, Member> getMap() {
        return memberMap;
    }

    @Override
    public void put(UUID key, Member value) {
        memberMap.put(key, value);
    }

    @Override
    public void putAll(List<Member> collection) {
        collection.forEach(member -> memberMap.put(member.getUUID(), member));
    }

    @Override
    public void remove(UUID key, Member value) {
        memberMap.remove(key, value);
    }

    @Override
    public Member get(UUID key) {
        for(Member member : memberMap.values()) {
            if(isSamePlayer(member, key)) return member;
        } return null;
    }

    public Member get(Player player, Team team) {
        Member member = get(player);

        if(member == null) return null;

        if(member.getTeam().getName().equalsIgnoreCase(team.getName())) return member;

        return null;
    }

    public Member get(Player player) {
        return get(player.getUniqueId());
    }

    public boolean isSamePlayer(Member member, UUID key) {
        return member.getUUID().compareTo(key) == 0;
    }
}
