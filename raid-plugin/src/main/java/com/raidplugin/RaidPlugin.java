package com.raidplugin;

import com.raidplugin.api.prototype.Team;
import com.raidplugin.sdk.json.JSONProvider;
import com.raidplugin.sdk.manager.WallManager;
import com.raidplugin.sdk.repository.TeamRepository;
import com.raidplugin.sdk.repository.member.MemberRepository;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class RaidPlugin extends JavaPlugin {

    public static RaidPlugin getInstance() {
        return getPlugin(RaidPlugin.class);
    }

    private final File archive = Paths.get(getDataFolder() + "/database/database.json").toFile();

    private final TeamRepository teamRepository = TeamRepository.getInstance();
    private final JSONProvider jsonProvider = JSONProvider.getInstance();
    private final MemberRepository memberRepository = MemberRepository.getInstance();
    private final WallManager wallManager = WallManager.getInstance();

    @Override
    public void onLoad() {
        if(!getDataFolder().exists()) getDataFolder().mkdirs();

        if(!archive.exists()) {
            try {
                archive.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            } return;
        }

        List<Team> teamList = jsonProvider.deserialize(List.class, archive);

        teamList.forEach(team -> {
            team.getMembers().forEach(member -> memberRepository.put(member.getUUID(), member));

            teamRepository.put(team.getName(), team);

            wallManager.getCollection().addAll(team.getWalls());
        });
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        jsonProvider.serialize(teamRepository.getMap().values(), archive, List.class);
    }

}
