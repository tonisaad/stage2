    //Student Name: SAAD FAROOQ
    //Student ID: 45718210

    import java.io.*;
    import java.net.*;
    import java.util.ArrayList;

    public class myClient2 {

      private int serverNumber;
      private static String serverType = "yes";
      private static int serverID = 0;
      private static int serverCore;
      private static int jobID;
      private static int min;
      private static int largestServerCount;
      private static int remainder;
      private static ArrayList < String > servers = new ArrayList < String > ();
      private static boolean flag = false;
      private static boolean flag1 = false;
      private static int serverCount;

      public static void main(String[] args) {

        // create a socket       
        try {
          Socket s = new Socket("127.0.0.1", 50000);
          DataOutputStream dout = new DataOutputStream(s.getOutputStream());
          BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

          System.out.println("Target IP: " + s.getInetAddress() + " Target Port: " + s.getPort());

          //Handshake HELO->OK->AUTH->OK
          dout.write(("HELO\n").getBytes());
          dout.flush();
          String str = in .readLine();
          System.out.println(str);

          String username = System.getProperty("user.name");
          System.out.println("SENT: AUTH" + username);
          dout.write(("AUTH " + username + "\n").getBytes());
          str = in .readLine();
          System.out.println("RCVD: " + str);

          //send REDY
          dout.write(("REDY\n").getBytes());
          dout.flush();
          str = in .readLine();

          //While last message from ds-server is not none
          while (!str.equals("NONE")) {
            System.out.println(str);

            System.out.println("RCVD: " + str);
            String[] jobInfo = str.split(" ");
            jobID = Integer.parseInt(jobInfo[2]);

            if (jobInfo[0].equals("JCPL")) {
              dout.write(("REDY\n").getBytes()); //send REDY
              dout.flush();
              str = in .readLine();
            } else if (jobInfo[0].equals("JOBN")) {

              servers.clear();
              flag = false;
              flag1 = false;

              if (flag1 = false) {
                dout.write(("GETS Avail: " + jobInfo[4] + " " + jobInfo[5] + " " + jobInfo[6] + "\n").getBytes());
                dout.flush();
                str = in .readLine();
                System.out.println("RCVD: " + str);

                String[] Info1 = str.split(" ");

                serverCount = Integer.parseInt(Info1[1]);

                dout.write(("OK\n").getBytes());
                dout.flush();

                dout.write(("OK\n").getBytes());
                dout.flush();
                if (serverCount == 0) {
                  flag1 = true;
                }
              }

              if (flag1 = true) {
                dout.write(("GETS Capable: " + jobInfo[4] + " " + jobInfo[5] + " " + jobInfo[6] + "\n").getBytes());
                dout.flush();
                str = in .readLine();
                System.out.println("RCVD: " + str);

                String[] Info = str.split(" ");
                serverCount = Integer.parseInt(Info[1]);

                dout.write(("OK\n").getBytes());
                dout.flush();

              }

              //handling server information

              if (flag != true) {
                for (int i = 0; i < serverCount; i++) {
                  str = in .readLine();

                  servers.add(str); //adding all the servers in the list 

                  String serverInfo[] = str.split(" ");

              
                }
              }
              dout.write(("OK\n").getBytes());
              dout.flush();
              str = in .readLine();
              System.out.println("RCVD: " + str);

              min = 1000000;

              System.out.println("server size: " + servers.size());

              for (int i = 0; i < servers.size(); i++) {
                String serverCheck[] = servers.get(i).split(" ");
                if (Integer.parseInt(serverCheck[7]) == 0 || Integer.parseInt(serverCheck[8]) == 0) {
                  flag = true;
                  serverType = serverCheck[0];
                  serverID = Integer.parseInt(serverCheck[1]);

                } else {
                  flag = false;
                }
              }

              //finding number of largest servers
              if (flag != true) {
                for (int i = 0; i < servers.size(); i++) {
                  String string[] = servers.get(i).split(" ");
                  dout.write(("EJWT " + string[0] + " " + string[1] + "\n").getBytes());
                  dout.flush();
                  str = in .readLine();
                  // System.out.println("aa: " + str );

                  String wtime = str;

                  if (min > (Integer.parseInt(str))) {
                    min = Integer.parseInt(str);
                    serverType = string[0];
                    serverID = Integer.parseInt(string[1]);

                  }
                }
              }

              

              //schedule a job
              dout.write(("SCHD " + jobID + " " + serverType + " " + serverID + "\n").getBytes());
              dout.flush();
              str = in .readLine();
              System.out.println("RCVD: " + str);

              dout.write(("REDY\n").getBytes());
              dout.flush();
              str = in .readLine();
            }
          }

          // send QUIT 
          System.out.println("SENT: QUIT");
          dout.write(("QUIT\n").getBytes());
          str = in .readLine();
          System.out.println("RCVD: " + str);

          in .close();
          dout.close();
          s.close();

        } catch (Exception e) {
          System.out.println(e);
        }
      }
    }
