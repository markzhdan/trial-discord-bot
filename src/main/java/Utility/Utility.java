package Utility;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;

public class Utility
{
    public boolean roleExists(List<Role> allServerRoles, String name)
    {
        for(Role role : allServerRoles)
        {
            if(role.getName().equalsIgnoreCase(name))
            {
                return true;
            }
        }
        return false;
    }

    public Role findRole(Member member, String name) {
        List<Role> roles = member.getRoles();
        return roles.stream()
                .filter(role -> role.getName().equals(name)) // filter by role name
                .findFirst() // take first result
                .orElse(null); // else return null
    }
}
