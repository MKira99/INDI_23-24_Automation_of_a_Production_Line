package MES_Logic;

import MAIN.MES_DB_SQL;
import MAIN.MES_ERP_MTCP;

import static MES_Logic.PieceProduction.quantity;

public class Piece {
    public static int Conclusionday;
    public static int totalmachineTime;
    public static int timeST3;
    public static int timeST5;
    public static int timePT5;
    public static int timePT6;
    public static int flag = 0;
    public static int TotalPieceTime;
    public static int MediumPieceTime;
    public static long PieceLineTime[] = new long[quantity+1];


  //  public static void  ProducePiece(int orderID, int pieceNr,int quantity,int ArrivingDay,int StartingDay) throws Exception {
        public static void  ProducePiece() throws Exception {
            System.out.println("Start Day Of Production: " +Threader.DayTimer.getDay());

/*
            switch (PieceProduction.pieceNr) {
                case 3:
                    System.out.println("Starting Production of Piece 3");
                    Piece2to3.Produce2to3();
                    Piece2to3.stopFlag = true;
                    if (Piece2to3.Time2to3_ST3s != 0) {
                        System.out.println("TotalMachineTime_3: " + Piece2to3.Time2to3_ST3s);
                        System.out.println("Mean time_3: " + Piece2to3.MediumTimePiece2to3);
                        System.out.println("Conclusion Day_3: " + Piece2to3.ConclusionDay);
                        for (int Piece = 1; Piece <= quantity; Piece++) {
                            PieceLineTime[Piece] = Piece2to3.timerPecaStore2to3[Piece];
                            System.out.println("Piece: "+Piece+ " Time: "+PieceLineTime[Piece]);
                        }

                        TotalPieceTime = (int) Piece2to3.TotalPieceTime;
                        flag = 1;
                        Conclusionday = Piece2to3.ConclusionDay;
                        timeST3 = (int) Piece2to3.Time2to3_ST3s/1000;
                        totalmachineTime = timeST3;

                        //client.OrderCompleted("localhost");
                    }
                    break;
                case 4:
                    System.out.println("IM ON 4 ");
                    Piece2to4.Produce2to4();
                    System.out.println("Starting Production of Piece 4");
                    Piece2to4.Produce2to4();
                    Piece2to4.stopFlag = true;
                    if (Piece2to4.Time2to4_PT5s != 0) {
                        System.out.println("TotalMachineTime_4: " + Piece2to4.Time2to4_PT5s);
                        System.out.println("Mean time_4: " + Piece2to4.MediumTimePiece2to4);
                        System.out.println("Conclusion Day_4: " + Piece2to4.ConclusionDay);
                        for (int Piece = 1; Piece <= quantity; Piece++) {
                            PieceLineTime[Piece] = Piece2to4.timerPecaStore2to4[Piece];
                            System.out.println("Piece: "+Piece+ " Time: "+PieceLineTime[Piece]);
                        }

                        TotalPieceTime = (int) Piece2to4.TotalPieceTime;
                        flag = 1;
                        Conclusionday = Piece2to4.ConclusionDay;
                        timePT5 = (int) Piece2to4.Time2to4_PT5s/1000;
                        totalmachineTime = timePT5;

                        //client.OrderCompleted("localhost");
                    }
                    break;
                    case 5:
                    System.out.println("IM ON 5 NIGGA");
                    Piece2to5.Produce2to5();
                        if (Piece2to5.MediumTimePiece2to5 != 0){
                        System.out.println("Time PT5 Motor 2to5: " +Piece2to5.Time2to5_PT5s);
                        System.out.println("Time PT5 Motor 2to5: " +Piece2to5.Time2to5_PT6s);
                        System.out.println("2to5 mean time: " +Piece2to5.MediumTimePiece2to5);
                        System.out.println("TotalMachineTime " +Piece2to5.TotalMachineTime);
                        System.out.println("Conclusion Day 2to5  " +Piece2to5.ConclusionDay);
                        TotalPieceTime = (int) Piece2to5.MediumTimePiece2to5;
                        flag = 1;

                        Conclusionday = (int) Piece2to5.ConclusionDay;
                        timePT5 = (int) Piece2to5.Time2to5_PT5s/1000;
                        timePT6 = (int) Piece2to5.Time2to5_PT6s/1000;
                        totalmachineTime = timePT5+timePT6;

                        // client.OrderCompleted("localhost");
                    }
                        break;

                case 6:
                    System.out.println("Starting Production of Piece 6");
                    Piece1to6.Produce1to6();
                    Piece1to6.stopFlag = true;
                    if (Piece1to6.Time1to6_ST5s != 0) {
                        System.out.println("TotalMachineTime_6: " + Piece1to6.Time1to6_ST5s);
                        System.out.println("Mean time_6: " + Piece1to6.MediumTimePiece1to6);
                        System.out.println("Conclusion Day_6: " + Piece1to6.ConclusionDay);
                        for (int Piece = 1; Piece <= quantity; Piece++) {
                            PieceLineTime[Piece] = Piece1to6.timerPecaStore1to6[Piece];
                            System.out.println("Piece: "+Piece+ " Time: "+PieceLineTime[Piece]);
                        }

                        TotalPieceTime = (int) Piece1to6.TotalPieceTime;
                        flag = 1;
                        Conclusionday = Piece1to6.ConclusionDay;
                        timeST5 = (int) Piece1to6.Time1to6_ST5s/1000;
                        totalmachineTime = timeST5;

                        //client.OrderCompleted("localhost");
                    }
                    break;
                case 7:
                    System.out.println("IM ON 7");
                    Piece2to7.Produce2to7();
                    if (Piece2to7.MediumTimePiece2to7 != 0) {

                        System.out.println("Time ST5 Motor 2to7: " +Piece2to7.Time2to7_PT5s);
                        System.out.println("Time PT5 Motor 2to7: " +Piece2to7.Time2to7_PT6s);
                        System.out.println("2to7 mean time: " +Piece2to7.MediumTimePiece2to7);
                        System.out.println("TotalMachineTime " +Piece2to7.TotalMachineTime);
                        System.out.println("Conclusion Day 2to7  " +Piece2to7.ConclusionDay);
                        for (int Piece = 1; Piece <= quantity; Piece++) {
                            PieceLineTime[Piece] = Piece2to7.timerPecaStore2to7[Piece];
                            System.out.println("Piece: "+Piece+ "Time: "+PieceLineTime[Piece]);
                        }

                        TotalPieceTime = (int) Piece2to7.MediumTimePiece2to7;
                        flag = 1;
                        Conclusionday = (int) Piece2to7.ConclusionDay;
                        timePT5 = (int) Piece2to7.Time2to7_PT5s/1000;
                        timePT6 = (int) Piece2to7.Time2to7_PT6s/1000;
                        totalmachineTime = timePT5+timePT6;

                        // client.OrderCompleted("localhost");
                    }

                    break;
                case 8:
                    System.out.println("Starting Production of Piece 8");
                    Piece1to8.Produce1to8();
                    if (Piece1to8.Time1to8_ST5s != 0) {
                        System.out.println("Time PT5s_8: " + Piece1to8.Time1to8_PT5s);
                        System.out.println("Time ST5s_8: " + Piece1to8.Time1to8_ST5s);
                        System.out.println("TotalMachineTime_8: " + Piece1to8.TotalMachineTime);
                        System.out.println("Mean time_8: " + Piece1to8.MediumTimePiece1to8);
                        System.out.println("Conclusion Day_8: " + Piece1to6.ConclusionDay);
                        for (int Piece = 1; Piece <= quantity; Piece++) {
                            PieceLineTime[Piece] = Piece1to8.timerPecaStore1to8[Piece];
                            System.out.println("Piece: "+Piece+ "Time: "+PieceLineTime[Piece]);
                        }
                        flag = 1;
                        TotalPieceTime = (int) Piece1to8.TotalPieceTime;
                        Conclusionday = Piece1to8.ConclusionDay;
                        timeST5 = (int) Piece1to8.Time1to8_ST5s/1000;
                        timePT5 = (int) Piece1to8.Time1to8_PT5s/1000;
                        totalmachineTime = timeST5+timePT5;

                    }
                    break;
                case 9:
                System.out.println("IM ON 9 ");
                Piece2to9.Produce2to9();

                if (Piece2to9.Time2to9_PT5s != 0) {
                    System.out.println("Time PT5 Motor 2to9: " +Piece2to9.Time2to9_PT5s);
                    System.out.println("Time PT5 Motor 2to9: " +Piece2to9.Time2to9_PT6s);
                    System.out.println("2to5 mean time: " +Piece2to9.MediumTimePiece2to9);
                    System.out.println("TotalMachineTime " +Piece2to9.TotalMachineTime);
                    System.out.println("Conclusion Day 2to9  " +Piece2to9.ConclusionDay);
                    for (int Piece = 1; Piece <= quantity; Piece++) {
                        PieceLineTime[Piece] = Piece2to9.timerPecaStore2to9[Piece];
                        System.out.println("Piece: "+Piece+ "Time: "+PieceLineTime[Piece]);
                    }

                    TotalPieceTime = (int) Piece2to9.MediumTimePiece2to9;
                    flag = 1;
                    Conclusionday = Piece2to9.ConclusionDay;
                    timePT5 = (int) Piece2to9.Time2to9_PT5s/1000;
                    timePT6 = (int) Piece2to9.Time2to9_PT6s/1000;
                    totalmachineTime = timePT5+timePT6;

                    // client.OrderCompleted("localhost");
                }
                break;
                default:
                    throw new Exception("Invalid piece number: " + PieceProduction.pieceNr);
            }
*/
            if(flag == 1) {
                flag = 0;
                MES_DB_SQL.orderCompleted(); //Coloco na db "ORDER" status 3
                MES_DB_SQL.setNextOrder();   //Meto Flag a true ERRO
                PieceProduction.onhold = 0;

                MES_DB_SQL.ProducedOrderCompleted(); //Coloco interface ERRO
                System.out.println("CURRENT SERVER DAY:" +Threader.DayTimer.getDay());
                MES_ERP_MTCP.OrderCompleted("10.227.144.133");
            }
        }

}

