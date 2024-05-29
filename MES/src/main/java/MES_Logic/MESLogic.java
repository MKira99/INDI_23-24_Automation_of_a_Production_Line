package MES_Logic;
import java.lang.Math;


public class MESLogic {

    public static int mach_c1_top_des_tool;
    public static int mach_c1_bot_des_tool;
    public static int mach_c2_top_des_tool;
    public static int mach_c2_bot_des_tool;
    public static int mach_c3_top_des_tool;
    public static int mach_c3_bot_des_tool;

    public static Command load1 = new Command();
    public static Command load2 = new Command();
    public static Command load3 = new Command();
    public static Command load4 = new Command();


    public static Command cell0_1 = new Command();
    public static Command cell1_1 = new Command();
    public static Command cell2_1 = new Command();
    public static Command cell3_1 = new Command();
    public static Command cell4_1 = new Command();
    public static Command cell5_1 = new Command();
    public static Command cell6_1 = new Command();

    public static Command cell0_2 = new Command();
    public static Command cell1_2 = new Command();
    public static Command cell2_2 = new Command();
    public static Command cell3_2 = new Command();
    public static Command cell4_2 = new Command();
    public static Command cell5_2 = new Command();
    public static Command cell6_2 = new Command();

    public static Command unload1 = new Command();
    public static Command unload2 = new Command();
    public static Command unload3 = new Command();
    public static Command unload4 = new Command();

    public static boolean usageOfCell_2 = false;

    public static Order TreatOrder;

    public MESLogic(Order newOrder) {
        TreatOrder = newOrder;
    }

    public static void main() {

        String PieceType = TreatOrder.getPieceType();
        short Quantity = TreatOrder.getQuantity();

        //Load


        short temp_quantity = Quantity;
        temp_quantity = (short) Math.round(Quantity/4);

        System.out.println("temp_quantity: " + temp_quantity);

        if(Quantity%4 == 0){
            load1.quantity = temp_quantity;
            load2.quantity = temp_quantity;
            load3.quantity = temp_quantity;
            load4.quantity = temp_quantity;
        }
        else if(Quantity%4 == 1){
            load1.quantity = (short) (temp_quantity + 1);
            load2.quantity = temp_quantity;
            load3.quantity = temp_quantity;
            load4.quantity = temp_quantity;
        }
        else if(Quantity%4 == 2){
            load1.quantity = (short) (temp_quantity + 1);
            load2.quantity = (short) (temp_quantity + 1);
            load3.quantity = temp_quantity;
            load4.quantity = temp_quantity;
        }
        else if(Quantity%4 == 3){
            load1.quantity = (short) (temp_quantity + 1);
            load2.quantity = (short) (temp_quantity + 1);
            load3.quantity = (short) (temp_quantity + 1);
            load4.quantity = temp_quantity;
        }

        load1.id = "load1_" + TreatOrder.getOrderID();
        load2.id = "load2_" + TreatOrder.getOrderID();
        load3.id = "load3_" + TreatOrder.getOrderID();
        load4.id = "load4_" + TreatOrder.getOrderID();

        System.out.println("load1 : " + load1.id);
        System.out.println("load2 : " + load2.id);
        System.out.println("load3 : " + load3.id);
        System.out.println("load4 : " + load4.id);

        load1.tool1 = 0;
        load2.tool1 = 0;
        load3.tool1 = 0;
        load4.tool1 = 0;

        load1.tool2 = 0;
        load2.tool2 = 0;
        load3.tool2 = 0;
        load4.tool2 = 0;


        if (inttype(TreatOrder.getPieceType()) == (short) 7 || inttype(TreatOrder.getPieceType()) == (short) 8 || inttype(TreatOrder.getPieceType()) == (short) 9) {
            load1.type = 2;
            load2.type = 2;
            load3.type = 2;
            load4.type = 2;
        } else {
            load1.type = 1;
            load2.type = 1;
            load3.type = 1;
            load4.type = 1;
        }


        //Unload



        unload1.quantity = load1.quantity;
        unload2.quantity = load2.quantity;
        unload3.quantity = load3.quantity;
        unload4.quantity = load4.quantity;
        
        unload1.id = "unload1_" + TreatOrder.getOrderID();
        unload2.id = "unload2_" + TreatOrder.getOrderID();
        unload3.id = "unload3_" + TreatOrder.getOrderID();
        unload4.id = "unload4_" + TreatOrder.getOrderID();

        unload1.type = inttype(PieceType);
        unload2.type = inttype(PieceType);
        unload3.type = inttype(PieceType);
        unload4.type = inttype(PieceType);

        unload1.tool1 = 0;
        unload2.tool1 = 0;
        unload3.tool1 = 0;
        unload4.tool1 = 0;

        unload1.tool2 = 0;
        unload2.tool2 = 0;
        unload3.tool2 = 0;
        unload4.tool2 = 0;


        //Cells
        
        switch (PieceType) {
            case "P3":

                cell1_1.id = "cell1_1_" + TreatOrder.getOrderID();
                cell2_1.id = "cell2_1_" + TreatOrder.getOrderID();
                cell3_1.id = "cell3_1_" + TreatOrder.getOrderID();
                cell4_1.id = "cell4_1_" + TreatOrder.getOrderID();
                cell5_1.id = "cell5_1_" + TreatOrder.getOrderID();
                cell6_1.id = "cell6_1_" + TreatOrder.getOrderID();

                temp_quantity = (short) Math.round(Quantity/6);
                
                if(Quantity%6 == 0){
                    cell1_1.quantity = temp_quantity;
                    cell2_1.quantity = temp_quantity;
                    cell3_1.quantity = temp_quantity;
                    cell4_1.quantity = temp_quantity;
                    cell5_1.quantity = temp_quantity;
                    cell6_1.quantity = temp_quantity;
                }
                else if(Quantity%6 == 1){
                    cell1_1.quantity = (short) (temp_quantity + 1);
                    cell2_1.quantity = temp_quantity;
                    cell3_1.quantity = temp_quantity;
                    cell4_1.quantity = temp_quantity;
                    cell5_1.quantity = temp_quantity;
                    cell6_1.quantity = temp_quantity;
                }
                else if(Quantity%6 == 2){
                    cell1_1.quantity = (short) (temp_quantity + 1);
                    cell2_1.quantity = (short) (temp_quantity + 1);
                    cell3_1.quantity = temp_quantity;
                    cell4_1.quantity = temp_quantity;
                    cell5_1.quantity = temp_quantity;
                    cell6_1.quantity = temp_quantity;
                }
                else if(Quantity%6 == 3){
                    cell1_1.quantity = (short) (temp_quantity + 1);
                    cell2_1.quantity = (short) (temp_quantity + 1);
                    cell3_1.quantity = (short) (temp_quantity + 1);
                    cell4_1.quantity = temp_quantity;
                    cell5_1.quantity = temp_quantity;
                    cell6_1.quantity = temp_quantity;
                }
                else if(Quantity%6 == 4){
                    cell1_1.quantity = (short) (temp_quantity + 1);
                    cell2_1.quantity = (short) (temp_quantity + 1);
                    cell3_1.quantity = (short) (temp_quantity + 1);
                    cell4_1.quantity = (short) (temp_quantity + 1);
                    cell5_1.quantity = temp_quantity;
                    cell6_1.quantity = temp_quantity;
                }
                else if(Quantity%6 == 5){
                    cell1_1.quantity = (short) (temp_quantity + 1);
                    cell2_1.quantity = (short) (temp_quantity + 1);
                    cell3_1.quantity = (short) (temp_quantity + 1);
                    cell4_1.quantity = (short) (temp_quantity + 1);
                    cell5_1.quantity = (short) (temp_quantity + 1);
                    cell6_1.quantity = temp_quantity;
                }
            
                cell1_1.type = 1;
                cell2_1.type = 1;
                cell3_1.type = 1;
                cell4_1.type = 1;
                cell5_1.type = 1;
                cell6_1.type = 1;

                cell1_1.tool1 = 1;
                cell2_1.tool1 = 1;
                cell3_1.tool1 = 1;
                cell4_1.tool1 = 1;
                cell5_1.tool1 = 1;
                cell6_1.tool1 = 1;

                cell1_1.tool2 = 0;
                cell2_1.tool2 = 0;
                cell3_1.tool2 = 0;
                cell4_1.tool2 = 0;
                cell5_1.tool2 = 0;
                cell6_1.tool2 = 0;



                break;
            
            case "P4":

                cell1_1.id = "cell1_1_" + TreatOrder.getOrderID();
                cell2_1.id = "cell2_1_" + TreatOrder.getOrderID();
                cell3_1.id = "cell3_1_" + TreatOrder.getOrderID();
                cell4_1.id = "cell4_1_" + TreatOrder.getOrderID();
                cell5_1.id = "cell5_1_" + TreatOrder.getOrderID();
                cell6_1.id = "cell6_1_" + TreatOrder.getOrderID();

                
                temp_quantity = (short) Math.round(Quantity/6);

                if(Quantity%6 == 0){
                    cell1_1.quantity = temp_quantity;
                    cell2_1.quantity = temp_quantity;
                    cell3_1.quantity = temp_quantity;
                    cell4_1.quantity = temp_quantity;
                    cell5_1.quantity = temp_quantity;
                    cell6_1.quantity = temp_quantity;
                }
                else if(Quantity%6 == 1){
                    cell1_1.quantity = (short) (temp_quantity + 1);
                    cell2_1.quantity = temp_quantity;
                    cell3_1.quantity = temp_quantity;
                    cell4_1.quantity = temp_quantity;
                    cell5_1.quantity = temp_quantity;
                    cell6_1.quantity = temp_quantity;
                }
                else if(Quantity%6 == 2){
                    cell1_1.quantity = (short) (temp_quantity + 1);
                    cell2_1.quantity = (short) (temp_quantity + 1);
                    cell3_1.quantity = temp_quantity;
                    cell4_1.quantity = temp_quantity;
                    cell5_1.quantity = temp_quantity;
                    cell6_1.quantity = temp_quantity;
                }
                else if(Quantity%6 == 3){
                    cell1_1.quantity = (short) (temp_quantity + 1);
                    cell2_1.quantity = (short) (temp_quantity + 1);
                    cell3_1.quantity = (short) (temp_quantity + 1);
                    cell4_1.quantity = temp_quantity;
                    cell5_1.quantity = temp_quantity;
                    cell6_1.quantity = temp_quantity;
                }
                else if(Quantity%6 == 4){
                    cell1_1.quantity = (short) (temp_quantity + 1);
                    cell2_1.quantity = (short) (temp_quantity + 1);
                    cell3_1.quantity = (short) (temp_quantity + 1);
                    cell4_1.quantity = (short) (temp_quantity + 1);
                    cell5_1.quantity = temp_quantity;
                    cell6_1.quantity = temp_quantity;
                }
                else if(Quantity%6 == 5){
                    cell1_1.quantity = (short) (temp_quantity + 1);
                    cell2_1.quantity = (short) (temp_quantity + 1);
                    cell3_1.quantity = (short) (temp_quantity + 1);
                    cell4_1.quantity = (short) (temp_quantity + 1);
                    cell5_1.quantity = (short) (temp_quantity + 1);
                    cell6_1.quantity = temp_quantity;
                }


                cell1_1.type = 1;
                cell2_1.type = 1;
                cell3_1.type = 1;
                cell4_1.type = 1;
                cell5_1.type = 1;
                cell6_1.type = 1;

                cell1_1.tool1 = 1;
                cell2_1.tool1 = 1;
                cell3_1.tool1 = 1;
                cell4_1.tool1 = 1;
                cell5_1.tool1 = 1;
                cell6_1.tool1 = 1;

                cell1_1.tool2 = 2;
                cell2_1.tool2 = 2;
                cell3_1.tool2 = 2;
                cell4_1.tool2 = 0;
                cell5_1.tool2 = 0;
                cell6_1.tool2 = 0;

                cell0_1.id = "cell0_1_" + TreatOrder.getOrderID();
                Quantity = (short) (cell4_1.quantity + cell5_1.quantity + cell6_1.quantity);
                cell0_1.quantity = Quantity;
                cell0_1.type = 3;
                cell0_1.tool1 = 0;
                cell0_1.tool2 = 0;

                usageOfCell_2 = true;

                cell1_2.id = "cell1_2_" + TreatOrder.getOrderID();
                cell2_2.id = "cell2_2_" + TreatOrder.getOrderID();
                cell3_2.id = "cell3_2_" + TreatOrder.getOrderID();
                cell4_2.id = "NULL";
                cell5_2.id = "NULL";
                cell6_2.id = "NULL";

                temp_quantity = (short) Math.round(Quantity/3);
                if(Quantity%3 == 0){
                    cell1_2.quantity = temp_quantity;
                    cell2_2.quantity = temp_quantity;
                    cell3_2.quantity = temp_quantity;
                    cell4_2.quantity = 0;
                    cell5_2.quantity = 0;
                    cell6_2.quantity = 0;
                }
                else if(Quantity%3 == 1){
                    cell1_2.quantity = (short) (temp_quantity + 1);
                    cell2_2.quantity = temp_quantity;
                    cell3_2.quantity = temp_quantity;
                    cell4_2.quantity = 0;
                    cell5_2.quantity = 0;
                    cell6_2.quantity = 0;
                }
                else if(Quantity%3 == 2){
                    cell1_2.quantity = (short) (temp_quantity + 1);
                    cell2_2.quantity = (short) (temp_quantity + 1);
                    cell3_2.quantity = temp_quantity;
                    cell4_2.quantity = 0;
                    cell5_2.quantity = 0;
                    cell6_2.quantity = 0;
                }

                cell1_2.type = 3;
                cell2_2.type = 3;
                cell3_2.type = 3;
                cell4_2.type = 0;
                cell5_2.type = 0;
                cell6_2.type = 0;

                cell1_2.tool1 = 0;
                cell2_2.tool1 = 0;
                cell3_2.tool1 = 0;
                cell4_2.tool1 = 0;
                cell5_2.tool1 = 0;
                cell6_2.tool1 = 0;

                cell1_2.tool2 = 2;
                cell2_2.tool2 = 2;
                cell3_2.tool2 = 2;
                cell4_2.tool2 = 0;
                cell5_2.tool2 = 0;
                cell6_2.tool2 = 0;

                break;

            case "P5":

                cell0_1.id = "cell0_1_" + TreatOrder.getOrderID();
                cell1_1.id = "cell1_1_" + TreatOrder.getOrderID();
                cell2_1.id = "cell2_1_" + TreatOrder.getOrderID();
                cell3_1.id = "cell3_1_" + TreatOrder.getOrderID();
                cell4_1.id = "cell4_1_" + TreatOrder.getOrderID();
                cell5_1.id = "cell5_1_" + TreatOrder.getOrderID();
                cell6_1.id = "cell6_1_" + TreatOrder.getOrderID();

                temp_quantity = (short) Math.round(Quantity/3);
                cell0_1.quantity = Quantity;
                if(Quantity%3 == 0){
                    cell1_1.quantity = temp_quantity;
                    cell2_1.quantity = temp_quantity;
                    cell3_1.quantity = temp_quantity;
                    cell4_1.quantity = temp_quantity;
                    cell5_1.quantity = temp_quantity;
                    cell6_1.quantity = temp_quantity;
                }
                else if(Quantity%3 == 1){
                    cell1_1.quantity = (short) (temp_quantity + 1);
                    cell2_1.quantity = temp_quantity;
                    cell3_1.quantity = temp_quantity;
                    cell4_1.quantity = (short) (temp_quantity + 1);
                    cell5_1.quantity = temp_quantity;
                    cell6_1.quantity = temp_quantity;
                }
                else if(Quantity%3 == 2){
                    cell1_1.quantity = (short) (temp_quantity + 1);
                    cell2_1.quantity = (short) (temp_quantity + 1);
                    cell3_1.quantity = temp_quantity;
                    cell4_1.quantity = (short) (temp_quantity + 1);
                    cell5_1.quantity = (short) (temp_quantity + 1);
                    cell6_1.quantity = temp_quantity;
                }
                
                cell0_1.type = 4;
                cell1_1.type = 1;
                cell2_1.type = 1;
                cell3_1.type = 1;
                cell4_1.type = 4;
                cell5_1.type = 4;
                cell6_1.type = 4;

                cell0_1.tool1 = 0;
                cell1_1.tool1 = 1;
                cell2_1.tool1 = 1;
                cell3_1.tool1 = 1;
                cell4_1.tool1 = 0;
                cell5_1.tool1 = 0;
                cell6_1.tool1 = 0;

                cell0_1.tool2 = 0;
                cell1_1.tool2 = 2;
                cell2_1.tool2 = 2;
                cell3_1.tool2 = 2;
                cell4_1.tool2 = 4;
                cell5_1.tool2 = 4;
                cell6_1.tool2 = 4;

                break;

            case "P6":

                cell1_1.id = "cell1_1_" + TreatOrder.getOrderID();
                cell2_1.id = "cell2_1_" + TreatOrder.getOrderID();
                cell3_1.id = "cell3_1_" + TreatOrder.getOrderID();
                cell4_1.id = "cell4_1_" + TreatOrder.getOrderID();
                cell5_1.id = "cell5_1_" + TreatOrder.getOrderID();
                cell6_1.id = "cell6_1_" + TreatOrder.getOrderID();

                temp_quantity = (short) Math.round(Quantity/6);
                cell0_1.quantity = Quantity;

                if(Quantity%6 == 0){
                    cell1_1.quantity = temp_quantity;
                    cell2_1.quantity = temp_quantity;
                    cell3_1.quantity = temp_quantity;
                    cell4_1.quantity = temp_quantity;
                    cell5_1.quantity = temp_quantity;
                    cell6_1.quantity = temp_quantity;
                }
                else if(Quantity%6 == 1){
                    cell1_1.quantity = (short) (temp_quantity + 1);
                    cell2_1.quantity = temp_quantity;
                    cell3_1.quantity = temp_quantity;
                    cell4_1.quantity = temp_quantity;
                    cell5_1.quantity = temp_quantity;
                    cell6_1.quantity = temp_quantity;
                }
                else if(Quantity%6 == 2){
                    cell1_1.quantity = (short) (temp_quantity + 1);
                    cell2_1.quantity = (short) (temp_quantity + 1);
                    cell3_1.quantity = temp_quantity;
                    cell4_1.quantity = temp_quantity;
                    cell5_1.quantity = temp_quantity;
                    cell6_1.quantity = temp_quantity;
                }
                else if(Quantity%6 == 3){
                    cell1_1.quantity = (short) (temp_quantity + 1);
                    cell2_1.quantity = (short) (temp_quantity + 1);
                    cell3_1.quantity = (short) (temp_quantity + 1);
                    cell4_1.quantity = temp_quantity;
                    cell5_1.quantity = temp_quantity;
                    cell6_1.quantity = temp_quantity;
                }
                else if(Quantity%6 == 4){
                    cell1_1.quantity = (short) (temp_quantity + 1);
                    cell2_1.quantity = (short) (temp_quantity + 1);
                    cell3_1.quantity = (short) (temp_quantity + 1);
                    cell4_1.quantity = (short) (temp_quantity + 1);
                    cell5_1.quantity = temp_quantity;
                    cell6_1.quantity = temp_quantity;
                }
                else if(Quantity%6 == 5){
                    cell1_1.quantity = (short) (temp_quantity + 1);
                    cell2_1.quantity = (short) (temp_quantity + 1);
                    cell3_1.quantity = (short) (temp_quantity + 1);
                    cell4_1.quantity = (short) (temp_quantity + 1);
                    cell5_1.quantity = (short) (temp_quantity + 1);
                    cell6_1.quantity = temp_quantity;
                }

                cell1_1.type = 1;
                cell2_1.type = 1;
                cell3_1.type = 1;
                cell4_1.type = 1;
                cell5_1.type = 1;
                cell6_1.type = 1;

                cell1_1.tool1 = 1;
                cell2_1.tool1 = 1;
                cell3_1.tool1 = 1;
                cell4_1.tool1 = 1;
                cell5_1.tool1 = 1;
                cell6_1.tool1 = 1;

                cell1_1.tool2 = 0;
                cell2_1.tool2 = 0;
                cell3_1.tool2 = 0;
                cell4_1.tool2 = 0;
                cell5_1.tool2 = 0;
                cell6_1.tool2 = 0;

                cell0_1.id = "cell0_1_" + TreatOrder.getOrderID();
                cell0_1.type = 3;
                cell0_1.tool1 = 0;
                cell0_1.tool2 = 0;

                usageOfCell_2 = true;

                cell1_2.id = "cell1_2_" + TreatOrder.getOrderID();
                cell2_2.id = "cell2_2_" + TreatOrder.getOrderID();
                cell3_2.id = "cell3_2_" + TreatOrder.getOrderID();
                cell4_2.id = "NULL";
                cell5_2.id = "NULL";
                cell6_2.id = "NULL";

                temp_quantity = (short) Math.round(Quantity/3);
                if(Quantity%3 == 0){
                    cell1_2.quantity = temp_quantity;
                    cell2_2.quantity = temp_quantity;
                    cell3_2.quantity = temp_quantity;
                    cell4_2.quantity = 0;
                    cell5_2.quantity = 0;
                    cell6_2.quantity = 0;
                }
                else if(Quantity%3 == 1){
                    cell1_2.quantity = (short) (temp_quantity + 1);
                    cell2_2.quantity = temp_quantity;
                    cell3_2.quantity = temp_quantity;
                    cell4_2.quantity = 0;
                    cell5_2.quantity = 0;
                    cell6_2.quantity = 0;
                }
                else if(Quantity%3 == 2){
                    cell1_2.quantity = (short) (temp_quantity + 1);
                    cell2_2.quantity = (short) (temp_quantity + 1);
                    cell3_2.quantity = temp_quantity;
                    cell4_2.quantity = 0;
                    cell5_2.quantity = 0;
                    cell6_2.quantity = 0;
                }

                cell1_2.type = 3;
                cell2_2.type = 3;
                cell3_2.type = 3;
                cell4_2.type = 0;
                cell5_2.type = 0;
                cell6_2.type = 0;

                cell1_2.tool1 = 2;
                cell2_2.tool1 = 2;
                cell3_2.tool1 = 2;
                cell4_2.tool1 = 0;
                cell5_2.tool1 = 0;
                cell6_2.tool1 = 0;

                cell1_2.tool2 = 2;
                cell2_2.tool2 = 2;
                cell3_2.tool2 = 2;
                cell4_2.tool2 = 0;
                cell5_2.tool2 = 0;
                cell6_2.tool2 = 0;

                break;

            case "P7":

                cell1_1.id = "cell1_1_" + TreatOrder.getOrderID();
                cell2_1.id = "cell2_1_" + TreatOrder.getOrderID();
                cell3_1.id = "cell3_1_" + TreatOrder.getOrderID();
                cell4_1.id = "cell4_1_" + TreatOrder.getOrderID();
                cell5_1.id = "cell5_1_" + TreatOrder.getOrderID();
                cell6_1.id = "cell6_1_" + TreatOrder.getOrderID();

                temp_quantity = (short) Math.round(Quantity/6);

                if(Quantity%6 == 0){
                    cell1_1.quantity = temp_quantity;
                    cell2_1.quantity = temp_quantity;
                    cell3_1.quantity = temp_quantity;
                    cell4_1.quantity = temp_quantity;
                    cell5_1.quantity = temp_quantity;
                    cell6_1.quantity = temp_quantity;
                }
                else if(Quantity%6 == 1){
                    cell1_1.quantity = temp_quantity;
                    cell2_1.quantity = temp_quantity;
                    cell3_1.quantity = temp_quantity;
                    cell4_1.quantity = temp_quantity;
                    cell5_1.quantity = temp_quantity;
                    cell6_1.quantity = (short) (temp_quantity + 1);
                }
                else if(Quantity%6 == 2){
                    cell1_1.quantity = temp_quantity;
                    cell2_1.quantity = temp_quantity;
                    cell3_1.quantity = temp_quantity;
                    cell4_1.quantity = temp_quantity;
                    cell5_1.quantity = (short) (temp_quantity + 1);
                    cell6_1.quantity = (short) (temp_quantity + 1);
                }
                else if(Quantity%6 == 3){
                    cell1_1.quantity = temp_quantity;
                    cell2_1.quantity = temp_quantity;
                    cell3_1.quantity = temp_quantity;
                    cell4_1.quantity = (short) (temp_quantity + 1);
                    cell5_1.quantity = (short) (temp_quantity + 1);
                    cell6_1.quantity = (short) (temp_quantity + 1);
                }
                else if(Quantity%6 == 4){
                    cell1_1.quantity = temp_quantity;
                    cell2_1.quantity = temp_quantity;
                    cell3_1.quantity = (short) (temp_quantity + 1);
                    cell4_1.quantity = (short) (temp_quantity + 1);
                    cell5_1.quantity = (short) (temp_quantity + 1);
                    cell6_1.quantity = (short) (temp_quantity + 1);
                }
                else if(Quantity%6 == 5){
                    cell1_1.quantity = temp_quantity;
                    cell2_1.quantity = (short) (temp_quantity + 1);
                    cell3_1.quantity = (short) (temp_quantity + 1);
                    cell4_1.quantity = (short) (temp_quantity + 1);
                    cell5_1.quantity = (short) (temp_quantity + 1);
                    cell6_1.quantity = (short) (temp_quantity + 1);
                }

                cell1_1.type = 2;
                cell2_1.type = 2;
                cell3_1.type = 2;
                cell4_1.type = 2;
                cell5_1.type = 2;
                cell6_1.type = 2;

                cell1_1.tool1 = 1;
                cell2_1.tool1 = 1;
                cell3_1.tool1 = 1;
                cell4_1.tool1 = 1;
                cell5_1.tool1 = 1;
                cell6_1.tool1 = 1;

                cell1_1.tool2 = 0;
                cell2_1.tool2 = 0;
                cell3_1.tool2 = 0;
                cell4_1.tool2 = 6;
                cell5_1.tool2 = 6;
                cell6_1.tool2 = 6;

                cell0_1.id = "cell0_1_" + TreatOrder.getOrderID();
                Quantity = (short) (cell1_1.quantity + cell2_1.quantity + cell3_1.quantity);
                cell0_1.quantity = Quantity;
                cell0_1.type = 8;
                cell0_1.tool1 = 0;
                cell0_1.tool2 = 0;

                usageOfCell_2 = true;

                cell1_2.id = "NULL";
                cell2_2.id = "NULL";
                cell3_2.id = "NULL";
                cell4_2.id = "cell4_2_" + TreatOrder.getOrderID();
                cell5_2.id = "cell5_2_" + TreatOrder.getOrderID();
                cell6_2.id = "cell6_2_" + TreatOrder.getOrderID();

                temp_quantity = (short) Math.round(Quantity/3);

                if(Quantity%3 == 0){
                    cell4_2.quantity = temp_quantity;
                    cell5_2.quantity = temp_quantity;
                    cell6_2.quantity = temp_quantity;
                }
                else if(Quantity%3 == 1){
                    cell4_2.quantity = (short) (temp_quantity + 1);
                    cell5_2.quantity = temp_quantity;
                    cell6_2.quantity = temp_quantity;
                }
                else if(Quantity%3 == 2){
                    cell4_2.quantity = (short) (temp_quantity + 1);
                    cell5_2.quantity = (short) (temp_quantity + 1);
                    cell6_2.quantity = temp_quantity;
                }


                cell4_2.type = 8;
                cell5_2.type = 8;
                cell6_2.type = 8;

                cell4_2.tool1 = 0;
                cell5_2.tool1 = 0;
                cell6_2.tool1 = 0;

                cell4_2.tool2 = 6;
                cell5_2.tool2 = 6;
                cell6_2.tool2 = 6;








                break;

            case "P8":

                cell1_1.id = "cell1_1_" + TreatOrder.getOrderID();
                cell2_1.id = "cell2_1_" + TreatOrder.getOrderID();
                cell3_1.id = "cell3_1_" + TreatOrder.getOrderID();
                cell4_1.id = "cell4_1_" + TreatOrder.getOrderID();
                cell5_1.id = "cell5_1_" + TreatOrder.getOrderID();
                cell6_1.id = "cell6_1_" + TreatOrder.getOrderID();

                temp_quantity = (short) Math.round(Quantity/6);

                if(Quantity%6 == 0){
                    cell1_1.quantity = temp_quantity;
                    cell2_1.quantity = temp_quantity;
                    cell3_1.quantity = temp_quantity;
                    cell4_1.quantity = temp_quantity;
                    cell5_1.quantity = temp_quantity;
                    cell6_1.quantity = temp_quantity;
                }
                else if(Quantity%6 == 1){
                    cell1_1.quantity = (short) (temp_quantity + 1);
                    cell2_1.quantity = temp_quantity;
                    cell3_1.quantity = temp_quantity;
                    cell4_1.quantity = temp_quantity;
                    cell5_1.quantity = temp_quantity;
                    cell6_1.quantity = temp_quantity;
                }
                else if(Quantity%6 == 2){
                    cell1_1.quantity = (short) (temp_quantity + 1);
                    cell2_1.quantity = (short) (temp_quantity + 1);
                    cell3_1.quantity = temp_quantity;
                    cell4_1.quantity = temp_quantity;
                    cell5_1.quantity = temp_quantity;
                    cell6_1.quantity = temp_quantity;
                }
                else if(Quantity%6 == 3){
                    cell1_1.quantity = (short) (temp_quantity + 1);
                    cell2_1.quantity = (short) (temp_quantity + 1);
                    cell3_1.quantity = (short) (temp_quantity + 1);
                    cell4_1.quantity = temp_quantity;
                    cell5_1.quantity = temp_quantity;
                    cell6_1.quantity = temp_quantity;
                }
                else if(Quantity%6 == 4){
                    cell1_1.quantity = (short) (temp_quantity + 1);
                    cell2_1.quantity = (short) (temp_quantity + 1);
                    cell3_1.quantity = (short) (temp_quantity + 1);
                    cell4_1.quantity = (short) (temp_quantity + 1);
                    cell5_1.quantity = temp_quantity;
                    cell6_1.quantity = temp_quantity;
                }
                else if(Quantity%6 == 5){
                    cell1_1.quantity = (short) (temp_quantity + 1);
                    cell2_1.quantity = (short) (temp_quantity + 1);
                    cell3_1.quantity = (short) (temp_quantity + 1);
                    cell4_1.quantity = (short) (temp_quantity + 1);
                    cell5_1.quantity = (short) (temp_quantity + 1);
                    cell6_1.quantity = temp_quantity;
                }

                cell1_1.type = 2;
                cell2_1.type = 2;
                cell3_1.type = 2;
                cell4_1.type = 2;
                cell5_1.type = 2;
                cell6_1.type = 2;

                cell1_1.tool1 = 1;
                cell2_1.tool1 = 1;
                cell3_1.tool1 = 1;
                cell4_1.tool1 = 1;
                cell5_1.tool1 = 1;
                cell6_1.tool1 = 1;

                cell1_1.tool2 = 0;
                cell2_1.tool2 = 0;
                cell3_1.tool2 = 0;
                cell4_1.tool2 = 0;
                cell5_1.tool2 = 0;
                cell6_1.tool2 = 0;



                    
                

                break;
        
            case "P9":

            cell1_1.id = "cell1_1_" + TreatOrder.getOrderID();
            cell2_1.id = "cell2_1_" + TreatOrder.getOrderID();
            cell3_1.id = "cell3_1_" + TreatOrder.getOrderID();
            cell4_1.id = "cell4_1_" + TreatOrder.getOrderID();
            cell5_1.id = "cell5_1_" + TreatOrder.getOrderID();
            cell6_1.id = "cell6_1_" + TreatOrder.getOrderID();

            temp_quantity = (short) Math.round(Quantity/6);

            if(Quantity%6 == 0){
                cell1_1.quantity = temp_quantity;
                cell2_1.quantity = temp_quantity;
                cell3_1.quantity = temp_quantity;
                cell4_1.quantity = temp_quantity;
                cell5_1.quantity = temp_quantity;
                cell6_1.quantity = temp_quantity;
            }
            else if(Quantity%6 == 1){
                cell1_1.quantity = temp_quantity;
                cell2_1.quantity = temp_quantity;
                cell3_1.quantity = temp_quantity;
                cell4_1.quantity = temp_quantity;
                cell5_1.quantity = temp_quantity;
                cell6_1.quantity = (short) (temp_quantity + 1);
            }
            else if(Quantity%6 == 2){
                cell1_1.quantity = temp_quantity;
                cell2_1.quantity = temp_quantity;
                cell3_1.quantity = temp_quantity;
                cell4_1.quantity = temp_quantity;
                cell5_1.quantity = (short) (temp_quantity + 1);
                cell6_1.quantity = (short) (temp_quantity + 1);
            }
            else if(Quantity%6 == 3){
                cell1_1.quantity = temp_quantity;
                cell2_1.quantity = temp_quantity;
                cell3_1.quantity = temp_quantity;
                cell4_1.quantity = (short) (temp_quantity + 1);
                cell5_1.quantity = (short) (temp_quantity + 1);
                cell6_1.quantity = (short) (temp_quantity + 1);
            }
            else if(Quantity%6 == 4){
                cell1_1.quantity = temp_quantity;
                cell2_1.quantity = temp_quantity;
                cell3_1.quantity = (short) (temp_quantity + 1);
                cell4_1.quantity = (short) (temp_quantity + 1);
                cell5_1.quantity = (short) (temp_quantity + 1);
                cell6_1.quantity = (short) (temp_quantity + 1);
            }
            else if(Quantity%6 == 5){
                cell1_1.quantity = temp_quantity;
                cell2_1.quantity = (short) (temp_quantity + 1);
                cell3_1.quantity = (short) (temp_quantity + 1);
                cell4_1.quantity = (short) (temp_quantity + 1);
                cell5_1.quantity = (short) (temp_quantity + 1);
                cell6_1.quantity = (short) (temp_quantity + 1);
            }
                
            cell1_1.type = 2;
            cell2_1.type = 2;
            cell3_1.type = 2;
            cell4_1.type = 2;
            cell5_1.type = 2;
            cell6_1.type = 2;

            cell1_1.tool1 = 1;
            cell2_1.tool1 = 1;
            cell3_1.tool1 = 1;
            cell4_1.tool1 = 1;
            cell5_1.tool1 = 1;
            cell6_1.tool1 = 1;

            cell1_1.tool2 = 0;
            cell2_1.tool2 = 0;
            cell3_1.tool2 = 0;
            cell4_1.tool2 = 0;
            cell5_1.tool2 = 0;
            cell6_1.tool2 = 0;

            cell0_1.id = "cell0_1_" + TreatOrder.getOrderID();
            cell0_1.quantity = Quantity;
            cell0_1.type = 8;
            cell0_1.tool1 = 0;
            cell0_1.tool2 = 0;

            usageOfCell_2 = true;

            cell1_2.id = "NULL";
            cell2_2.id = "NULL";
            cell3_2.id = "NULL";
            cell4_2.id = "cell4_2_" + TreatOrder.getOrderID();
            cell5_2.id = "cell5_2_" + TreatOrder.getOrderID();
            cell6_2.id = "cell6_2_" + TreatOrder.getOrderID();

            temp_quantity = (short) Math.round(Quantity/3);

            if(Quantity%3 == 0){
                cell4_2.quantity = temp_quantity;
                cell5_2.quantity = temp_quantity;
                cell6_2.quantity = temp_quantity;
            }
            else if(Quantity%3 == 1){
                cell4_2.quantity = (short) (temp_quantity + 1);
                cell5_2.quantity = temp_quantity;
                cell6_2.quantity = temp_quantity;
            }
            else if(Quantity%3 == 2){
                cell4_2.quantity = (short) (temp_quantity + 1);
                cell5_2.quantity = (short) (temp_quantity + 1);
                cell6_2.quantity = temp_quantity;
            }


            cell4_2.type = 8;
            cell5_2.type = 8;
            cell6_2.type = 8;

            cell4_2.tool1 = 5;
            cell5_2.tool1 = 5;
            cell6_2.tool1 = 5;

            cell4_2.tool2 = 0;
            cell5_2.tool2 = 0;
            cell6_2.tool2 = 0;

            break;

            default:
            System.out.println("Invalid PieceType");
                break;
        }
        
    }

    public static Command[] getCommand() {
        
        Command[] commands = new Command[22];
        commands[0] = load1;
        commands[1] = load2;
        commands[2] = load3;
        commands[3] = load4;
        commands[4] = cell0_1;
        commands[5] = cell1_1;
        commands[6] = cell2_1;
        commands[7] = cell3_1;
        commands[8] = cell4_1;
        commands[9] = cell5_1;
        commands[10] = cell6_1;
        commands[11] = cell0_2;
        commands[12] = cell1_2;
        commands[13] = cell2_2;
        commands[14] = cell3_2;
        commands[15] = cell4_2;
        commands[16] = cell5_2;
        commands[17] = cell6_2;
        commands[18] = unload1;
        commands[19] = unload2;
        commands[20] = unload3;
        commands[21] = unload4;

        return commands;
    }

    public static int[] getMachDesTool() {
        int[] machDesTool = new int[6];
        machDesTool[0] = mach_c1_top_des_tool;
        machDesTool[1] = mach_c1_bot_des_tool;
        machDesTool[2] = mach_c2_top_des_tool;
        machDesTool[3] = mach_c2_bot_des_tool;
        machDesTool[4] = mach_c3_top_des_tool;
        machDesTool[5] = mach_c3_bot_des_tool;

        return machDesTool;
    }

    public static short inttype(String input) {
        // Remove non-numeric characters
        String numericPart = input.replaceAll("[^0-9]", "");
    
        // Convert to short
        short number = Short.parseShort(numericPart);
    
        return number;
    }    

    public static boolean getUsageOfCell_2() {
        return usageOfCell_2;
    }
}


