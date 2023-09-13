package fr.pantheonsorbonne.ufr27.miage.auth.discord;

public record DiscordUserResponse(
        String id,
        String username,
        String avatag,
        String discriminator,
        Integer public_flags,
        Integer flags,
        String banner,
        String accent_color,
        String global_name,
        String avatar_decoration_data,
        String banner_color,
        Boolean mfa_enabled,
        String locale,
        Integer premium_type,
        String email,
        Boolean verified) {

}
