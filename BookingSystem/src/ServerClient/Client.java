/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerClient;

import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author
 */
public class Client {

    public static void main(String args[]) throws Exception {
        
        int port;
        InetAddress server = null;
        
        boolean Error = false;
        boolean Quit = false;

        while (true && !Error && !Quit) {

            List<String> message;
            List<String> facilityList = null;

            String responseString = new String();
            boolean timeout = true;

            Message rcvMessage = null;

            RequestMessage reqFacility = new RequestMessage();
            byte[] reqFac = new byte[1024];
            byte[] rcvFaciity = new byte[1024];
            boolean finish = false;
            boolean end = false;
            DatagramSocket clientSocket = new DatagramSocket();
            int count;
            boolean received = false;

            // making the the request to get the updated facility list

            reqFacility.setRequest(8);
            reqFacility.setRequestID(getRequestID());
            reqFac = Marshaller.marshall(reqFacility);

            //get the IP address and port number from the user
            System.out.print("Please Enter the IP Address: ");
            Scanner IPScan = new Scanner(System.in);

            String lIPAddr = IPScan.next();

            String[] lIP = lIPAddr.split("\\.");


            server = InetAddress.getByName(lIPAddr);
            System.out.print("Please Enter the Port No.: ");

            Scanner portScan = new Scanner(System.in);

            port = portScan.nextInt();

            //send the facility list request message to server
            while (!finish && !Quit) {

                DatagramPacket sendFacility = new DatagramPacket(reqFac, reqFac.length, server, port);
                count = 3;

                while (timeout && count != 0) {
                    clientSocket.send(sendFacility);
                    DatagramPacket receiveFacility = new DatagramPacket(rcvFaciity, rcvFaciity.length);

                    //set the timeout to 10s
                    clientSocket.setSoTimeout(10000);

                    //receive the message from server
                    try {
                        while (!received) {
                            clientSocket.receive(receiveFacility);
                            while (receiveFacility.getLength() != 0 && !finish) {
                                byte[] data = receiveFacility.getData();

                                // do the unmarshaller
                                rcvMessage = Marshaller.unmarshall(data);
                                received = true;
                                timeout = false;
                                finish = true;
                                //break the loop and print out the result!
                            }
                        }
                    } catch (SocketTimeoutException e) {

                        //when timeout print error message and resend

                        System.out.println("Timeout reached!!! " + e);
                        System.out.println("Resend Request Message now");
                        timeout = true;
                        count--;
                    }

                }

                if (count == 0) {
                    /*
                     * if there are no response after the message resend three
                     * time, It will print out error and quit!
                     */
                    finish = true;
                    System.out.println("Error ");
                    Error = true;
                    Quit = true;
                } else {

                    ResponseMessage fac = (ResponseMessage) rcvMessage;
                    facilityList = fac.getResponseMessages();

                }
            }

            while (!end && !Quit) {


                if (!Error) {

                    //print out the menu
                    Start(facilityList);

                    //get request input from the user
                    Scanner input = new Scanner(System.in);

                    String temp = input.next();

                    String[] messages = temp.split(",|\\.");

                    message = Arrays.asList(messages);

                    if (new Integer(message.get(0)) == 7) {
                        Quit = true;

                    } else {
                        RequestMessage reqMessage = makeMessage(message);
                        byte[] sendBuffer = new byte[2048];
                        byte[] rcvBuffer = new byte[2048];

                        sendBuffer = Marshaller.marshall(reqMessage);
                        System.out.println("Sending data : " + new String(sendBuffer, "UTF-8"));

                        //Start transmit/retransmit message 
                        finish = false;
                        timeout = true;
                        while (!finish) {

                            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, server, port);
                            count = 5;
                            while (timeout) {
                                clientSocket.send(sendPacket);

                                DatagramPacket receivePacket = new DatagramPacket(rcvBuffer, rcvBuffer.length);


                                received = false;
                                /*
                                 * if the user want to observe the facility the
                                 * timeout will set to infinty else the timeout
                                 * is 20s
                                 */
                                if (new Integer(message.get(0)) == 4) {
                                    clientSocket.setSoTimeout(0);
                                } else {
                                    clientSocket.setSoTimeout(20000);
                                }

                                try {
                                    while (!received) {
                                        clientSocket.receive(receivePacket);
                                        while (receivePacket.getLength() != 0 && !finish) {
                                            byte[] data = receivePacket.getData();


                                            // do the unmarshaller
                                            rcvMessage = Marshaller.unmarshall(data);

                                            received = true;
                                            timeout = false;
                                            finish = true;
                                            //break the loop and print out the result!
                                        }
                                    }
                                } catch (SocketTimeoutException e) {
                                    //when timeout print error messageand resend

                                    System.out.println("Timeout reached!!! " + e);
                                    System.out.println("Resend Message now");
                                    timeout = true;
                                    count--;
                                }

                                if (count == 0) {
                                    timeout = false;
                                    finish = true;
                                }
                            }

                            if (count != 0) {
                                System.out.print("Response Message: ");
                                //Error message
                                if (rcvMessage.getMessageType() == -1) {
                                    ExceptionMessage a = (ExceptionMessage) rcvMessage;
                                    responseString = a.getExceptionMessage();
                                    System.out.println(responseString);
                                } // response message
                                else if (rcvMessage.getMessageType() == 2) {
                                    ResponseMessage a = (ResponseMessage) rcvMessage;
                                    List<String> responseList = a.getResponseMessages();

                                    if (responseList.size() == 0) {

                                        responseList.add("no message response!");
                                    }
                                    responseString = responseList.get(responseList.size() - 1);

                                    if (!a.isRequestSuccessful()) {
                                        System.out.println("Request Unsuccessful" + responseString);
                                    } else {
                                        System.out.println("Request successful. " + responseString);
                                    }
                                }


                            } else {
                                System.out.println("Server Not Found!");
                            }

                        }
                    }
                }
            }
        }
        System.out.println("-----------END-----------");
    }

    static RequestMessage makeMessage(List<String> message) {
        RequestMessage reqMessage = new RequestMessage();

        int requestIndex = new Integer(message.get(0));

        //set the request ID of the request
        reqMessage.setRequestID(getRequestID());
        reqMessage.setRequest(requestIndex);

        //make the message in different type
        switch (requestIndex) {

            case 1: {

                reqMessage.setAttribute("facility", message.get(1));
                reqMessage.setAttribute("startDate", message.get(2));
                reqMessage.setAttribute("endDate", message.get(3));
                reqMessage.setUsesServerLog(Boolean.valueOf(message.get(4)));
                break;

            }
            case 2: {
                reqMessage.setAttribute("bookingID", message.get(1));
                reqMessage.setAttribute("changeIndicator", message.get(2));
                reqMessage.setAttribute("hours", message.get(3));
                reqMessage.setUsesServerLog(Boolean.valueOf(message.get(4)));
                break;
            }
            case 3: {
                reqMessage.setAttribute("facility", message.get(1));

                String days = message.get(2).replace('&', ',');
                reqMessage.setAttribute("days", days);
                reqMessage.setUsesServerLog(Boolean.valueOf(message.get(3)));
                break;

            }
            case 4: {
                reqMessage.setAttribute("facility", message.get(1));
                reqMessage.setAttribute("interval", message.get(2));
                reqMessage.setUsesServerLog(Boolean.valueOf(message.get(3)));
                break;

            }
            case 5: {
                reqMessage.setAttribute("bookingID", message.get(1));
                reqMessage.setUsesServerLog(Boolean.valueOf(message.get(2)));
                break;
            }
            case 6: {
                reqMessage.setUsesServerLog(Boolean.valueOf(message.get(1)));
                break;
            }
        }

        return reqMessage;

    }

    public static void Start(List<String> pFacility) {
        String lFacility = "";

        for (int i = 0; i < pFacility.size(); i++) {
            lFacility = lFacility + pFacility.get(i) + " ";
        }
        // print out the menu
        String startMsg = "\nWelcome to Booking System!\n\n"
                + "facility list: " + lFacility + "\n\n"
                + "please select the following three options:(by index)\n"
                + "1. Make a new booking\n([1,name,start,end.])\n\n"
                + "2. Change a booking\n([2,confirmation ID,advance/postpone,offset.])\n\n"
                + "3. Check avaiablity of a facility.\n([3,facility name,days by &.])\n\n"
                + "4. Monitor a facility\n(4,facility name,interval.])\n\n"
                + "5. Cancel a booking\n([5,confirmation ID.])\n\n"
                + "6. Check all facilities' availability\n([6.])\n\n"
                + "7. Quit Booking System.\n\n\n"
                + "Please input it here: \n\n";

        System.out.println(startMsg);
    }

    public static int getRequestID() {
        //generate the request ID
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("MMddhhmmss");

        int requestID = new Integer(df.format(date));
        return requestID;
    }
}
