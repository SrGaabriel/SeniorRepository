package com.raidplugin.sdk.event.member;

import com.raidplugin.api.prototype.member.Member;
import com.raidplugin.api.prototype.member.type.Role;
import com.raidplugin.sdk.event.wrapper.EventWrapper;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class MemberPromoteEvent extends EventWrapper {

    private final Member member;
    private final Role role;
    private final CommandSender author;

    public MemberPromoteEvent(Member member, Role role, CommandSender sender) {
        this.member = member; this.role = role; this.author = sender;

        Bukkit.getPluginManager().callEvent(this);
    }

    public Member getMember() {
        return member;
    }

    public CommandSender getAuthor() {
        return author;
    }

    public Role getRole() {
        return role;
    }
}
