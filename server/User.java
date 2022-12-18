package server;

import java.util.*;

public class User {
    String name;
    public  List<String> pending = Collections.synchronizedList(new ArrayList<>());
    

    
    public List<Integer> pendingId = Collections.synchronizedList(new ArrayList<>());
    Boolean[][] timetable = {{true, true, true, true},{true, true, true, true},{true, true, true, true},{true, true, true, true},{true, true, true, true,},{true, true, true, true},{true, true, true, true}};
    
    public User(String u_name)
    {
        name = u_name;
    }
    

    public void apendPending(String meet, int id)
    {
        pending.add(meet);
        pendingId.add(id);
    } 

    public Boolean[][] getTimetable()
    {
        return timetable;
    }

    public Boolean getTime(int i, int j)
        {
            return timetable[i][j];
        }
    public String getName()
    {
        return name;
    }
    public void setTime(int i, int j)
        {
            timetable[i][j] = !timetable[i][j];
        }

    public static String matchTimes(User user1, User user2)
    {
        int day = 0;
        int time;
        for (; day < 7; day++)
        {
            for (time = 0; time < 4; time++)
            {
                if (user1.timetable[day][time] && user2.timetable[day][time])
                {
                    return(new String("day: " + (day + 1) + " time: " + (time + 1) + "\n" ));
                }
            }
        }
        return (new String("No matching time"));
    }
}
