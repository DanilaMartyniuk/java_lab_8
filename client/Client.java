package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main (String[] args)

    {
        Scanner input = new Scanner(System.in);
        try(
            Socket socket = new Socket("127.0.0.1", 8000);

            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());

            DataInputStream  reader = new DataInputStream(socket.getInputStream());
        )
        {
            while (true)
            {
                String req = reader.readUTF();
                if (!req.isEmpty())
                {
                     System.out.println(req);
                }

                String name = input.nextLine();
                writer.writeUTF(name);
                writer.flush();
                System.out.println(reader.readUTF());

                while(true)
                {
                    System.out.println("1. Adjust schedule \n2. Make an appointment\n3.Approove pending appoinments\n0.exit");
                    String command = input.nextLine();
                    switch(command)
                                {
                                    case("1"):
                                    {
                                        writer.writeUTF(command);
                                        writer.flush();
                                        String timetable =reader.readUTF();
                                        System.out.println(timetable);
                                        System.out.println("enter a day");
                                        String day, time;
                                        while(true)
                                        {
                                        day = input.nextLine();
                                        if(day.equalsIgnoreCase("mon") ||
                                         day.equalsIgnoreCase("tue")||
                                          day.equalsIgnoreCase("wen") ||
                                          day.equalsIgnoreCase("thu") ||
                                          day.equalsIgnoreCase("fri") ||
                                          day.equalsIgnoreCase("sun") ||
                                          day.equalsIgnoreCase("sat"))
                                          {
                                            break;
                                          }
                                          else{
                                            System.out.println("Try again");
                                          }
                                        }
                                        writer.writeUTF(day);
                                        writer.flush();
                                        System.out.println("Choose time");
                                        while(true)
                                        {
                                            time = input.nextLine();
                                            if(time.equals("1") ||
                                            time.equals("2")||
                                            time.equals("3") ||
                                            time.equals("4"))
                                          {
                                            break;
                                          }
                                          else{
                                            System.out.println("Try again");
                                          }


                                        }
                                        writer.writeInt(Integer.parseInt(time));
                                        writer.flush();
                                        

                                        break;


                                    }
                                    case("2"):
                                    {
                                        writer.writeUTF(command);
                                        writer.flush();
                                        System.out.println(reader.readUTF());
                                        String in = input.nextLine();
                                        writer.writeInt(Integer.parseInt(in));
                                        System.out.println(reader.readUTF());
                                        while(true)
                                        {
                                            in = input.nextLine();
                                            if(in.equals("1") || in.equals("0"))
                                            {
                                                break;
                                            }
                                            System.out.println("try again");
                                        }
                                        
                                        if (in.equals("1"))
                                        {
                                            writer.writeUTF(in);
                                            writer.flush();
                                        }
                                        System.out.println("Wait for an approovment");
                                        break;
                                    }
                                    case("3"):
                                    {
                                        writer.writeUTF(command);
                                        writer.flush();
                                        System.out.println("Pending appointments  Enter 0 to exit");
                                        System.out.println(reader.readUTF());
                                        System.out.println("Enter id to approove");
                                        String approovedId = input.nextLine();
                                        writer.writeUTF(approovedId);
                                        writer.flush();
                                        System.out.println("Approoved");
                                        break;
                                    }
                                    case("0"):
                                    {
                                        writer.writeUTF(command);
                                        writer.flush();
                                        writer.close();
                                        reader.close();
                                        socket.close();
                                        System.exit(0);
                                    }
                                }
                }
            }
           
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        

    }
}
