package Utility;

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
}
