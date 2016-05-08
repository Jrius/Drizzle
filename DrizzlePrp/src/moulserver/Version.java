/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import shared.*;
import moulserver.Server.*;
import prpobjects.Guid;

public class Version
{

    Integer buildId;
    Integer buildType;
    Integer branchId;
    Guid productId;
    Integer port;
    String gateserver_mod;
    String gateserver_B;
    Integer gateserver_base;
    String csrserver_mod;
    String csrserver_B;
    Integer csrserver_base;
    String gameserver_mod;
    String gameserver_B;
    Integer gameserver_base;
    String authserver_mod;
    String authserver_B;
    Integer authserver_base;
    String gateserver;
    String authserver;
    String httpserver;
    String fileserver;



    static Version moul_9_853 = new Version(); //the final version of moul.
    static Version moulagain_1_893 = new Version();
    static Version moulagain_1_902 = new Version();
    static Version talcum = new Version();
    static Version magiquest_1_1407 = new Version();
    //static Version currentMoulagain = moulagain_1_893;
    static Version currentMoulagain = moulagain_1_902;

    private static final String enc_mod_talcum = "d7d3dcd3cd8be358a463a88bc40044d331d9f7d3fc12881a68ac405819d9e2b3405f7f64a698a1cdeaeb71ebb170904832b0a6652739058d7373c37047ff7ce1";
    public static final String enc_b_talcum = "b4573c3b62cb002b15e5501d1de44c65d25caced0f87753ab424ebf2a3db2979ded97782b15f655f7abac3b2030792f103b2369acde7e7d98675352057d421fb";

    static
    {
        moulagain_1_902.buildId = 902;
        moulagain_1_902.buildType = 50;
        moulagain_1_902.branchId = 1;
        moulagain_1_902.productId = new Guid(b.HexStringToBytes("219848ea356cd04b9daebb17c585e680"));
        moulagain_1_902.gateserver_mod = "8DFA35E6F87A5050AB254B81D1D77A23A39A210F34AB662E169855B6FC49D550DCB84C4DC7DBF11C154C55F5920D6AEC60BC55FA292F6FC3D72180A36B4423B5";
        moulagain_1_902.gateserver_B =   "B388FF0B90702B2E07BC6298839D0F0539FA3539A9F3B3FCCD5EA9A6610F9B380F9CBEA0BE6F7FE47CCBC4096C8DCE47688232C58994F9CA6969D06019B7F31A";
        moulagain_1_902.gateserver_base = 4;
        moulagain_1_902.csrserver_mod =  "BDF80317ED27740D768E7A8C88EBECDED773378F4BBCAE75F8DA22EE50C9B369970BE5289891F74497DF70F1866CFA037C4773B162004299F0C6E705D8CDD66D";
        moulagain_1_902.csrserver_B =    "031D8362369263A6A86631BE2842C2E686983A81130EB7965E27876A06685121F374E911E9735FDB372A2D49A47612065836596DBB59572C34E45ED42C78C334";
        moulagain_1_902.csrserver_base =  97;
        moulagain_1_902.gameserver_mod = "38A4F76943D7A6C727723B126A76095DAA9A3844DE8422C4DE3FFD5026EABB20D6AD2ECDFEF2701B19D18215D7CB336020BC43C9550BC920D924D249D0284B90";
        moulagain_1_902.gameserver_B =   "F9B293BD02BD7248A921BD093E44653EC2FD5AF0993B3F5C47765C1F84D80168DC5FB6BAC8FD79986293367F14CF33678404CFA37A65E9684F5858B38F3DDB3D";
        moulagain_1_902.gameserver_base = 73;
        moulagain_1_902.authserver_mod = "2D171942EB718F91297C61884375E5EE72FE451B43C38EB9475E03C80C78B7E44D315BCB66C2541A0A61115738669B346BAB6D12123887C53F20BE97A3A6568F";
        moulagain_1_902.authserver_B =   "21CF1DC608C023AD5336CE6125DDB95505C1BB8FF40D59F920279AEEFB235BEBE5EC01552ED564EFEA43B19EB98C753FDAB2BBB36A3DCDBCFA6F03F355D8E91B";
        moulagain_1_902.authserver_base = 41;
        //67.202.54.141 is the file server, which while hardcoded, should be ignored since it asks the gateserver.
        //184.73.198.22 is the gate *and* auth server, and that's where the confusion was.
        //moulagain_1_893.gateserver = "67.202.54.141";
        moulagain_1_902.gateserver = "184.73.198.22";
        moulagain_1_902.authserver = "184.73.198.22";
        moulagain_1_902.port = 14617;

        moulagain_1_893.buildId = 893;
        moulagain_1_893.buildType = 50;
        moulagain_1_893.branchId = 1;
        moulagain_1_893.productId = new Guid(b.HexStringToBytes("219848ea356cd04b9daebb17c585e680"));
        moulagain_1_893.gateserver_mod = "8DFA35E6F87A5050AB254B81D1D77A23A39A210F34AB662E169855B6FC49D550DCB84C4DC7DBF11C154C55F5920D6AEC60BC55FA292F6FC3D72180A36B4423B5";
        moulagain_1_893.gateserver_B =   "B388FF0B90702B2E07BC6298839D0F0539FA3539A9F3B3FCCD5EA9A6610F9B380F9CBEA0BE6F7FE47CCBC4096C8DCE47688232C58994F9CA6969D06019B7F31A";
        moulagain_1_893.gateserver_base = 4;
        moulagain_1_893.csrserver_mod =  "BDF80317ED27740D768E7A8C88EBECDED773378F4BBCAE75F8DA22EE50C9B369970BE5289891F74497DF70F1866CFA037C4773B162004299F0C6E705D8CDD66D";
        moulagain_1_893.csrserver_B =    "031D8362369263A6A86631BE2842C2E686983A81130EB7965E27876A06685121F374E911E9735FDB372A2D49A47612065836596DBB59572C34E45ED42C78C334";
        moulagain_1_893.csrserver_base =  97;
        moulagain_1_893.gameserver_mod = "38A4F76943D7A6C727723B126A76095DAA9A3844DE8422C4DE3FFD5026EABB20D6AD2ECDFEF2701B19D18215D7CB336020BC43C9550BC920D924D249D0284B90";
        moulagain_1_893.gameserver_B =   "F9B293BD02BD7248A921BD093E44653EC2FD5AF0993B3F5C47765C1F84D80168DC5FB6BAC8FD79986293367F14CF33678404CFA37A65E9684F5858B38F3DDB3D";
        moulagain_1_893.gameserver_base = 73;
        moulagain_1_893.authserver_mod = "2D171942EB718F91297C61884375E5EE72FE451B43C38EB9475E03C80C78B7E44D315BCB66C2541A0A61115738669B346BAB6D12123887C53F20BE97A3A6568F";
        moulagain_1_893.authserver_B =   "21CF1DC608C023AD5336CE6125DDB95505C1BB8FF40D59F920279AEEFB235BEBE5EC01552ED564EFEA43B19EB98C753FDAB2BBB36A3DCDBCFA6F03F355D8E91B";
        moulagain_1_893.authserver_base = 41;
        //67.202.54.141 is the file server, which while hardcoded, should be ignored since it asks the gateserver.
        //184.73.198.22 is the gate *and* auth server, and that's where the confusion was.
        //moulagain_1_893.gateserver = "67.202.54.141";
        moulagain_1_893.gateserver = "184.73.198.22";
        moulagain_1_893.authserver = "184.73.198.22";
        moulagain_1_893.port = 14617;
        //moulagain_1_893.authserver = "10.172.18.176"; //returned
        //moulagain_1_893.authserver = "176.18.172.10"; //returned
        //moulagain_1_893.authserver = "67.202.54.141";
        //moulagain_1_893.gateserver = "184.73.198.22";

        magiquest_1_1407.buildId = 1407; //guessing
        magiquest_1_1407.buildType = 50; //guessing
        magiquest_1_1407.branchId = 1; //guessing
        magiquest_1_1407.productId = new Guid(b.HexStringToBytes("00000000000000000000000000000000")); //wrong!
        magiquest_1_1407.csrserver_mod =  "90BFEE9B9597EB549C2C6F40B0ADFCAA7C63C8C20693109D9AA6AE7E0387EE54024D9E83F81A64E7E642551697B0C2E61C86553079045DD39EAB67FB99A20EA3";
        magiquest_1_1407.csrserver_B =    "B77425B4FA5A02CFF0B45206FD5BCD82458C417C20CEB9817A58A15A74589969A5684E46E5AB04E63EFBD2CD7A35542D9AC8BAF8FA85078C145BA82DC4689936";
        magiquest_1_1407.csrserver_base = 8311;
        magiquest_1_1407.gameserver_mod = "44F9F0759B399C6B064CA9B6168DD5A52AEE19E3938FE7A8F58FB130F022AA6D00E3D0F9A26C2A4EB9244E392F59278CDF8FF514B31795CA09E3034D5C981D90";
        magiquest_1_1407.gameserver_B =   "A10AA15D4959EE492522E720C269F19CA0711636F2939D50DE2A0F94465E16E77FD8E34F48EC6EEAC8D0E3E3765BD78178F1C95C0C7EB8412392FAA0603EB158";
        magiquest_1_1407.gameserver_base = 9923; //sub829170 //(num,num,addr,num,addr,num,num(*),B,mod)
        magiquest_1_1407.authserver_mod = "ADD33500787388A8D986F1C9A6D52415564D658F6FA89D4D414AAD7CD56902A621716A307DA726275B0C403C32841F0CBB3758A56038C746F265DF679A8E3EF9";
        magiquest_1_1407.authserver_B =   "D25633748D2F214928344EC6ED2AB479A67E68EA9FF980CE7ECFB31B2D13B3EEB0617FFF749BDABE139B29FA89031D46F8B41349B3DC4E16C95FD44A689B1288";
        magiquest_1_1407.authserver_base = 4057;
        //There is no gate server in magiquest_1_1407.
        //magiquest 1.1407 has at least 5 ip addresses embedded in it.
        magiquest_1_1407.httpserver = "support.cyanworlds.com"; //97.74.71.14
        magiquest_1_1407.fileserver = "174.129.220.215";
        magiquest_1_1407.authserver = "174.129.207.147";
        magiquest_1_1407.port = 14617;


        moul_9_853.buildId = 0;
        moul_9_853.buildType = 50;
        moul_9_853.branchId = 9;

//        talcum.gateserver_mod = enc_mod_talcum;
//        talcum.gateserver_B =   calculate_enc_B_talcum(4);
//        talcum.csrserver_mod =  enc_mod_talcum;
//        talcum.csrserver_B =    calculate_enc_B_talcum(97);
//        talcum.gameserver_mod = enc_mod_talcum;
//        talcum.gameserver_B =   calculate_enc_B_talcum(73);
//        talcum.authserver_mod = enc_mod_talcum;
//        talcum.authserver_B =   calculate_enc_B_talcum(41);
        talcum.gateserver_mod =  enc_mod_talcum;//SharedServer.talcum.gateserver_mod;
        talcum.gateserver_base = 4;
        talcum.gateserver_B =    SharedServer.calculate_enc_B_talcum(talcum.gateserver_base, enc_b_talcum, enc_mod_talcum);//SharedServer.talcum.gateserver_B;
        talcum.csrserver_mod =   enc_mod_talcum;//SharedServer.talcum.csrserver_mod;
        talcum.csrserver_base =  97;
        talcum.csrserver_B =     SharedServer.calculate_enc_B_talcum(talcum.csrserver_base, enc_b_talcum, enc_mod_talcum);//SharedServer.talcum.csrserver_B;
        talcum.gameserver_mod =  enc_mod_talcum;//SharedServer.talcum.gameserver_mod;
        talcum.gameserver_base = 73;
        talcum.gameserver_B =    SharedServer.calculate_enc_B_talcum(talcum.gameserver_base, enc_b_talcum, enc_mod_talcum);//SharedServer.talcum.gameserver_B;
        talcum.authserver_mod =  enc_mod_talcum;//SharedServer.talcum.authserver_mod;
        talcum.authserver_base = 41;
        talcum.authserver_B =    SharedServer.calculate_enc_B_talcum(talcum.authserver_base, enc_b_talcum, enc_mod_talcum);//SharedServer.talcum.authserver_B;
        talcum.buildId = 0;
        talcum.buildType = 50;
        talcum.branchId = 9;
        talcum.productId = new Guid(b.HexStringToBytes("219848ea356cd04b9daebb17c585e680"));
        talcum.gateserver = "dus.mine.nu";
        talcum.httpserver = "dus.mine.nu";
        talcum.fileserver = "dus.mine.nu";
        talcum.authserver = "dus.mine.nu";
        talcum.port = 14617;
    }


    /*public String getAddress(ServerType type)
    {
        switch(type)
        {
            case gate:
                return this.gateserver;
            case file:
                return this.fileserver;
            case auth:
                return this.authserver;
                case
        }
    }*/

}
