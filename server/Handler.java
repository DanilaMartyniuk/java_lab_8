package server;
import java.io.*;
import java.net.Socket;
import java.util.*;
public class Handler implements Runnable , AutoCloseable {
    private static Socket clientSocket;
    public static List<User> users = new ArrayList<User>();
    public Handler(Socket client) {
        Handler.clientSocket = client;
    }

    @Override
    public void run()
    {

            String call;
            try {
                DataOutputStream writer = new DataOutputStream(clientSocket.getOutputStream());
                DataInputStream reader = new DataInputStream(clientSocket.getInputStream());
                writer.writeUTF("Enter your login");
                writer.flush();
                call = reader.readUTF();
                System.out.println(call + " has connected");
                int userId = 0;
                Boolean exists = false;
                Boolean exit =false;
                for(int i = 0; i < users.size(); i++)
                {
                    String debug = users.get(i).getName();
                    if(debug.equals(call))
                    {
                        writer.writeUTF("Succsesfully logged in!");
                        writer.flush();
                        exists = true;
                        userId = i;
                        break;
                    }
                }
                    if (!exists){
                        users.add(new User(call));
                        userId = users.size() - 1;
                        writer.writeUTF("Created new user");
                        writer.flush();
                    }
                    while(true)
                    {
                        call = reader.readUTF();
                        switch(call)
                        {
                            case("1"):
                            {
                                showSchedule(userId, writer);
                                String day = reader.readUTF();
                                System.out.print(day);
                                int time = reader.readInt() - 1;
                                int iDay = convertDay(day) - 1;
                                users.get(userId).setTime(iDay, time);
                                break;

                            }
                            case("2"):
                            {
                                showUsers(writer);
                                int user2 = reader.readInt();
                                String ans = User.matchTimes(users.get(user2 - 1), users.get(userId));
                                writer.writeUTF(ans + " approove?\n1. Yes\n2.No");
                                String ans2 = reader.readUTF();
                                if(ans2.equals("1"))
                                {
                                    users.get(user2 - 1).apendPending(ans,userId);
                                }
                                break;
                            }
                            case("3"):
                            {
                                showPending(userId, writer);
                                String approovedId = reader.readUTF();
                                if(approovedId.equals("0"))
                                {
                                    break;
                                }
                                int id = Integer.parseInt(approovedId);

                                System.out.println(userId);
                                System.out.println(users.get(userId).pendingId.get(id-1));
                                System.out.println(id);
                                approove(userId, users.get(userId).pendingId.get(id-1), id );
                                break;
                            }
                            case("0"):
                            {
                                exit = true;
                                break;
                            }
                        }
                        if(exit)
                        {
                            break;
                        }
                    }
                    } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    private static void approove(int user1, int user2, int approoveId)
{
    int time, day;
    approoveId -= 1;
    day = (users.get(user1).pending.get(approoveId).charAt(5)) - '0' - 1;
    time = (users.get(user1).pending.get(approoveId).charAt(13)) - '0' - 1;
    users.get(user1).setTime(day, time);
    users.get(user2).setTime(day, time);
    users.get(user1).pending.remove(approoveId);
    users.get(user1).pendingId.remove(approoveId);
}
    
private static void showUsers(DataOutputStream writer)
{
    String output = "";
    for (int i = 0; i < users.size(); i++)
    {
        output += ((i+1)+". " + users.get(i).getName()+ "\n");
    }
    try {
        writer.writeUTF(output);
        writer.flush();
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}

    private static int convertDay(String day)
    {
        int iDay = 0;
        switch(day)
        {
            case ("mon"):
            {
                iDay = 1;
                break;
            }
            case ("tue"):
            {
                iDay = 2;
                break;
            }
            case ("wen"):
            {
                iDay = 3;
                break;
            }
            case ("thu"):
            {
                iDay = 4;
                break;
            }
            case ("fri"):
            {
                iDay = 5;
                break;
            }
            case ("sun"):
            {
                iDay = 6;
                break;
            }
            case ("sat"):
            {
                iDay = 7;
                break;
            }
        }
        return iDay;
    }

    private static void showPending(int id,DataOutputStream writer)
    {
        String resp = "";
        for (int i = 0; i < users.get(id).pending.size(); i++)
        {
            resp += (i + 1) + ". When: " + users.get(id).pending.get(i) + " From: " + users.get(users.get(id).pendingId.get(i)).getName() + "\n" ;
        } 
        if(users.get(id).pending.size() == 0)
        {
            resp += "0.No pending requests";
        }
        try {
            writer.writeUTF(resp);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void showSchedule(int id,DataOutputStream writer)
    {
        String schedule = "        mon tue wen thu fri sun sat\n";
        Boolean[][] timetable = users.get(id).getTimetable();
        for(int i = 0; i < 4; i++)
        {
            schedule += ((i+1) + ". " + (10 + i * 2) + "-" + (12 + i * 2) + " ");
            for(int j = 0; j < 7; j ++)
            {
                if(timetable[j][i])
                {
                    schedule += (1 + "   ");
                }
                else
                {
                    schedule += (0 + "   ");
                }
            }
            schedule += "\n";
        }
        schedule += "1 - free, 0 - reserved";
        try {
            writer.writeUTF(schedule);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
    }

    @Override
    public void close() throws Exception {
        clientSocket.close();
        
    }
}
