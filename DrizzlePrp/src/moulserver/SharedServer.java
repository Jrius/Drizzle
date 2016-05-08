///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
package moulserver;
//
import shared.*;
import java.util.Random;
//
public class SharedServer extends Thread
{
//
//    //_base is the base.
//    //_A is base^a (the client public key)
//    //_B is base^b (the server public key)
//    //_mod is the modulo.
//    //_key is base^ab (the shared secret key)
//
//    //These constants are all in little-endian form.
//
//    //static final String enc_mod_moulagain_1_893 = "BDF80317ED27740D768E7A8C88EBECDED773378F4BBCAE75F8DA22EE50C9B369970BE5289891F74497DF70F1866CFA037C4773B162004299F0C6E705D8CDD66D";
//    //static final String enc_B_moulagain_1_893 =   "031D8362369263A6A86631BE2842C2E686983A81130EB7965E27876A06685121F374E911E9735FDB372A2D49A47612065836596DBB59572C34E45ED42C78C334";
//
//    //static final String enc_mod_talcum = enc_mod_moulagain_1_893;
//    //static final String enc_mod_talcum = "a772e439e98d2e134108925429e63858ef81d9867d774e5e691d91126512c8101c73b8905d7a7b4eb67cf9fe1130dbffed10ce12a0f4ff32ba3a8d44f562ba8b";
//    //made with little-endian:
//    //static final String enc_B_talcum =            "d0984a8d9caa7f5942300cd4118dc55c63a5f47328ddad3de93ec48bc3dc49d3edaa54bfa79f4d7f27ee0dcdbbce5da8c836b4aa77fc27debe81ea7cf0e7ca58";
//    //made with big-endian:
//    //static final String enc_b_talcum = "2ca44d572da72ddaf9a9faa99ea54e376637e6b22306f426ef77dc6dfc7791eabf0ec1c51da702f68801d65c90401c46068c1a8dd8dc3e1ed4faee6cc9cd8c98";
//    //static final String enc_B_talcum = "aa87d4baf18b5eb7402a5db9230ff1da3335e4849acae69a6bcfa884bd526125ed8529baff1ddf1ed7743d506f735f75fe1bae8af09e7f5c0929f9cf1d8f8cd7";
//    static class VerInfo
//    {
//        String gateserver_mod;
//        String gateserver_B;
//        String csrserver_mod;
//        String csrserver_B;
//        String gameserver_mod;
//        String gameserver_B;
//        String authserver_mod;
//        String authserver_B;
//        int[] notthedroids;
//    }
//    static VerInfo moul_9_853 = new VerInfo(); //the final version of moul.
//    static VerInfo moulagain_1_893 = new VerInfo();
//    static VerInfo talcum = new VerInfo();
//
//    public static final String enc_mod_talcum = "d7d3dcd3cd8be358a463a88bc40044d331d9f7d3fc12881a68ac405819d9e2b3405f7f64a698a1cdeaeb71ebb170904832b0a6652739058d7373c37047ff7ce1";
//    public static final String enc_b_talcum = "b4573c3b62cb002b15e5501d1de44c65d25caced0f87753ab424ebf2a3db2979ded97782b15f655f7abac3b2030792f103b2369acde7e7d98675352057d421fb";
//    //public static final String enc_B_talcum = calculate_enc_B_talcum();
//
//    static{
//        moulagain_1_893.gateserver_mod = "8DFA35E6F87A5050AB254B81D1D77A23A39A210F34AB662E169855B6FC49D550DCB84C4DC7DBF11C154C55F5920D6AEC60BC55FA292F6FC3D72180A36B4423B5";
//        moulagain_1_893.gateserver_B =   "B388FF0B90702B2E07BC6298839D0F0539FA3539A9F3B3FCCD5EA9A6610F9B380F9CBEA0BE6F7FE47CCBC4096C8DCE47688232C58994F9CA6969D06019B7F31A";
//        moulagain_1_893.csrserver_mod =  "BDF80317ED27740D768E7A8C88EBECDED773378F4BBCAE75F8DA22EE50C9B369970BE5289891F74497DF70F1866CFA037C4773B162004299F0C6E705D8CDD66D";
//        moulagain_1_893.csrserver_B =    "031D8362369263A6A86631BE2842C2E686983A81130EB7965E27876A06685121F374E911E9735FDB372A2D49A47612065836596DBB59572C34E45ED42C78C334";
//        moulagain_1_893.gameserver_mod = "38A4F76943D7A6C727723B126A76095DAA9A3844DE8422C4DE3FFD5026EABB20D6AD2ECDFEF2701B19D18215D7CB336020BC43C9550BC920D924D249D0284B90";
//        moulagain_1_893.gameserver_B =   "F9B293BD02BD7248A921BD093E44653EC2FD5AF0993B3F5C47765C1F84D80168DC5FB6BAC8FD79986293367F14CF33678404CFA37A65E9684F5858B38F3DDB3D";
//        moulagain_1_893.authserver_mod = "2D171942EB718F91297C61884375E5EE72FE451B43C38EB9475E03C80C78B7E44D315BCB66C2541A0A61115738669B346BAB6D12123887C53F20BE97A3A6568F";
//        moulagain_1_893.authserver_B =   "21CF1DC608C023AD5336CE6125DDB95505C1BB8FF40D59F920279AEEFB235BEBE5EC01552ED564EFEA43B19EB98C753FDAB2BBB36A3DCDBCFA6F03F355D8E91B";
//        //talcum.gateserver_mod = "8DFA35E6F87A5050AB254B81D1D77A23A39A210F34AB662E169855B6FC49D550DCB84C4DC7DBF11C154C55F5920D6AEC60BC55FA292F6FC3D72180A36B4423B5";
//        //talcum.gateserver_B = calculate_enc_B_talcum();
//        talcum.gateserver_mod = enc_mod_talcum;
//        talcum.gateserver_B =   calculate_enc_B_talcum(4);
//        talcum.csrserver_mod =  enc_mod_talcum;
//        talcum.csrserver_B =    calculate_enc_B_talcum(97);
//        talcum.gameserver_mod = enc_mod_talcum;
//        talcum.gameserver_B =   calculate_enc_B_talcum(73);
//        talcum.authserver_mod = enc_mod_talcum;
//        talcum.authserver_B =   calculate_enc_B_talcum(41);
//        talcum.notthedroids = new int[]{0x5776aeed, 0x591eb23d, 0xeef5ddea, 0xc6718ce2};
//        moul_9_853.csrserver_mod =  "A772E439E98D2E134108925429E63858EF81D9867D774E5E691D91126512C8101C73B8905D7A7B4EB67CF9FE1130DBFFED10CE12A0F4FF32BA3A8D44F562BA8B";
//        moul_9_853.csrserver_B =    "F18028B1AD66BBCB6FCD530F0F0A1AF4B1263EB50396E7AC1D585F47189F0872A3513945F08FD5363A00D9170369932A6521294504C858E950B6C92574806D47";
//        moul_9_853.gameserver_mod = "8DFA35E6F87A5050AB254B81D1D77A23A39A210F34AB662E169855B6FC49D550DCB84C4DC7DBF11C154C55F5920D6AEC60BC55FA292F6FC3D72180A36B4423B5";
//        moul_9_853.gameserver_B =   "B388FF0B90702B2E07BC6298839D0F0539FA3539A9F3B3FCCD5EA9A6610F9B380F9CBEA0BE6F7FE47CCBC4096C8DCE47688232C58994F9CA6969D06019B7F31A";
//        moul_9_853.authserver_mod = "A772E439E98D2E134108925429E63858EF81D9867D774E5E691D91126512C8101C73B8905D7A7B4EB67CF9FE1130DBFFED10CE12A0F4FF32BA3A8D44F562BA8B";
//        moul_9_853.authserver_B =   "F18028B1AD66BBCB6FCD530F0F0A1AF4B1263EB50396E7AC1D585F47189F0872A3513945F08FD5363A00D9170369932A6521294504C858E950B6C92574806D47";
//    }
//
//    //public static final String enc_b_talcum =   "b42bb41a06c3af6e3218fec5802554ced126a1c5808bcdbeb716907d484a9403bb8f970426ac76fe071869c08b02bba1b32a365de6532d124c304d94b04fc84d";
//    //public static final String enc_mod_talcu = "";
    //public static String calculate_enc_B_talcum(long base2)
    public static String calculate_enc_B_talcum(long base2, String enc_b_talcum, String enc_mod_talcum)
    {
        //if(true) return "";
        //if(enc_B_talcum==null)
        //{
            LargeInteger b = new LargeInteger(enc_b_talcum);
            LargeInteger base = new LargeInteger(base2);
            LargeInteger mod = new LargeInteger(enc_mod_talcum);
            LargeInteger B = base.modPow(b, mod);
            //enc_B_talcum = B.toString(64);
            return B.toString(64);
        //}
        //return enc_B_talcum;
    }
//
    public static void generateAndPrintNewKeys()
    {
        Random rng = new Random();

        int method = 2;
        if(method==1)
        {
            LargeInteger b = new LargeInteger(512,rng);
            LargeInteger base = new LargeInteger(4);
            LargeInteger mod = new LargeInteger(Version.talcum.gateserver_mod);
            LargeInteger B = base.modPow(b, mod);
            m.msg("Private Key: ", b.toString(64));
            m.msg("Public Key: ", B.toString(64));
        }
        else if(method==2)
        {
            LargeInteger b = new LargeInteger(512,rng);
            LargeInteger mod = LargeInteger.probablePrime(512, rng);
            LargeInteger base = new LargeInteger(4);
            LargeInteger B = base.modPow(b, mod);
            m.msg("Private Key: ", b.toString(64));
            m.msg("Mod: ",mod.toString(64));
            m.msg("Public Key: ", B.toString(64));
        }
    }
//
//    //client calculations:
//    //LargeInteger _B = new LargeInteger(enc_x_moul);
//    //LargeInteger _mod = new LargeInteger(enc_n_moul);
//    //LargeInteger _a = new LargeInteger(512,rng);
//    //LargeInteger _base = new LargeInteger(4);
//    //LargeInteger _key = _B.modPow(_a, _mod);
//    //LargeInteger _A = _base.modPow(_a, _mod);
//
//    //server calculations:
//    //LargeInteger _B = new LargeInteger(enc_x_moul);
//    //LargeInteger _mod = new LargeInteger(enc_n_moul);
//    //LargeInteger _a = new LargeInteger(512,rng);
//    //LargeInteger _base = new LargeInteger(4);
//    //LargeInteger _key = _B.modPow(_a, _mod);
//    //LargeInteger _A = _base.modPow(_a, _mod);
//
}
