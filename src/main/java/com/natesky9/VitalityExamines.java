package com.natesky9;

import net.runelite.api.Client;
import net.runelite.client.plugins.itemstats.Effect;
import net.runelite.client.plugins.itemstats.ItemStatChangesService;
import net.runelite.client.plugins.itemstats.StatChange;

public class VitalityExamines {
    public static String fetch(Client client, ItemStatChangesService service, int itemId)
    {
        StringBuilder text = new StringBuilder();
        switch (itemId)
        {
            //region antipoison
            case 175: case 177: case 179: case 2446: case 11433: case 11435:
            case 25754: case 25755: case 25756: case 25757:
            {//normal antipoison
                text.append("<col=ff0000>When consumed: </col>");
                text.append("poison immunity for 90 seconds");
                break;
            }
            case 181: case 183: case 185: case 2448:
            case 25758: case 25759: case 25760: case 25761:
            {//super antipoison
                text.append("<col=ff0000>When consumed: </col>");
                text.append("poison immunity for 6 minutes");
                break;
            }
            case 5943: case 5945: case 5947: case 5949: case 11501: case 11503:
            case 25762: case 25763: case 25764: case 25765:
            {//antidote plus
                text.append("<col=ff0000>When consumed: </col>");
                text.append("poison immunity for 9 minutes");
                break;
            }
            case 5952: case 5954: case 5956: case 5958:
            {//antidote plus plus
                text.append("<col=ff0000>When consumed: </col>");
                text.append("venom immunity for 15 seconds and poison immunity for 12 minutes");
                break;
            }
            case 12905: case 12907: case 12909: case 12911:
            {//antivenom
                text.append("<col=ff0000>When consumed: </col>");
                text.append("venom immunity for 36 seconds and poison immunity for 12 minutes");
                break;            }
            case 12913: case 12915: case 12917: case 12919:
            {//antivenom plus
                text.append("<col=ff0000>When consumed: </col>");
                text.append("venom immunity for 3 minutes 36 seconds " +
                        "and poison immunity for 15 minutes");
                break;
            }
            case 29824: case 29827: case 29830: case 29833:
            {//extended antivenom plus
                text.append("<col=ff0000>When consumed: </col>");
                text.append("venom immunity for 6 minutes 18 seconds " +
                        "and poison immunity for 17 minutes 42 seconds");
                break;
            }
            //endregion antipoison
            //region antifire
            case 2452: case 2454: case 2456: case 2458: case 11505: case 11507:
            {
                text.append("<col=ff0000>When consumed: </col>");
                text.append("antifire resistance for 6 minutes");
                break;
            }
            case 11951: case 11953: case 11955: case 11957: case 11960: case 11962:
            {
                text.append("<col=ff0000>When consumed: </col>");
                text.append("antifire resistance for 12 minutes");
                break;
            }
            case 21978: case 21981: case 21984: case 21987: case 21994: case 21997:
            {
                text.append("<col=ff0000>When consumed: </col>");
                text.append("exceptional antifire resistance for 3 minutes");
                break;
            }
            case 22209: case 22212: case 22215: case 22218: case 22221: case 22224:
            {
                text.append("<col=ff0000>When consumed: </col>");
                text.append("exceptional antifire resistance for 6 minutes");
                break;
            }
            //endregion antifire
            //region surge potion
            case 30884: case 30881: case 30878: case 30875:
            {//every dose of the surge potion
                text.append("<col=ff0000>When consumed: </col>");
                text.append("Special attack + 25");
                break;
            }
            //endregion surge potion
            //region charting drinks
            case 31831:
            {//almost smuggled rum
                text.append("You take a whiff... smells safe to drink " +
                        "(boosts strength and drains attack)");
                break;
            }
            case 31832:
            {//sOrodamin's bru
                text.append("You take a whiff... something smells off " +
                        "(deals 5 damage and drains stats)");
                break;
            }
            case 31833:
            {//fish bladder stout
                text.append("You take a whiff... it smells suspicious " +
                        "(spawns an aggressive level 14 drink troll)");
                break;
            }
            case 31834:
            {//Potterington's marrow wine
                text.append("You take a whiff... smells safe to drink " +
                        "(boosts farming and drains agility");
                break;
            }
            case 31835:
            {//slug's mind balm
                text.append("You take a whiff... your head spins " +
                        "(takes you to Witchaven and drains magic. " +
                        "You sense that your boat would be nearby");
                break;
            }
            case 31836:
            {//spinner's last gasp
                text.append("You take a whiff... something smells off " +
                        "(inflicts poison starting at 4)");
                break;
            }
            case 31837:
            {//Barracuda brew
                text.append("You take a whiff... smells safe to drink. Or does it? " +
                        "You take another whiff, and it nearly knocks you off your feet! " +
                        "Best to drink this with plenty of room " +
                        "(DO NOT drink on a raft, it can kill you");
                break;
            }
            case 31838:
            {//banana daiquiri
                text.append("You take a whiff... smells safe to drink " +
                        "(transforms you into a banana)");
                break;
            }
            case 31839:
            {//Kharazi cooler
                text.append("You take a whiff... smells safe to drink " +
                        "(drains your run energy)");
                break;
            }
            case 31840:
            {//Dognose draught
                text.append("You take a whiff... smells safe to drink " +
                        "(boosts cooking and drains farming");
                break;
            }
            case 31841:
            {//the way home
                text.append("You take a whiff... your head spins " +
                        "(takes you outside your house. " +
                        "You sense that your boat would return to Port Sarim)");
                break;
            }
            case 31842:
            {//platinum rum
                text.append("You take a whiff... smells safe to drink " +
                        "(spawns seven drunken dwarves)");
                break;
            }
            case 31843:
            {//light in the dark
                text.append("You take a whiff... something smells off " +
                        "(deals 5 damage)");
                break;
            }
            case 31844:
            {//ogre prayer potion
                text.append("You take a whiff... something smells VERY off " +
                        "(deals 50% your hp)");
                break;
            }
            case 31845:
            {//get to the point punch
                text.append("You take a whiff... something smells off " +
                        "(deals 1 damage 5 times)");
                break;
            }
            case 31846:
            {//Oo'glug
                text.append("You take a whiff... smells safe to drink " +
                        "(acts as a stamina potion)");
                break;
            }
            case 31847:
            {//myths mighty mixer
                text.append("You take a whiff... something smells off " +
                        "(deals 25% your hp)");
                break;
            }
            case 31848:
            {//goldless asgoldian ale
                text.append("You take a whiff... smells safe to drink " +
                        "(takes 100 coins)");
                break;
            }
            case 31849:
            {//destructor's cocktail
                text.append("You take a whiff... something smells off " +
                        "(deals 33% your hp)");
                break;
            }
            case 31850:
            {//zogre's sloppy kisses
                text.append("You take a whiff... something smells off " +
                        "(inflicts disease and poison starting at 5)");
                break;
            }
            case 31851:
            {//creator's cocktail
                text.append("You take a whiff... something smells off " +
                        "(deals 33% your hp)");
                break;
            }
            case 31852:
            {//soul
                text.append("You take a whiff... You take another whiff... " +
                        "a sense of deja vu washes over you, and you feel extremely uneasy " +
                        "(This is a two-part drink - if you've sampled a Bottle of Soul Juice, " +
                        "this will deal 90% of your hp!)");
                break;
            }
            case 31853:
            {//zul-rye beer
                text.append("You take a whiff... something smells VERY off " +
                        "(inflicts venom. Exercise caution)");
                break;
            }
            case 31854:
            {//Captain Clop's mango gin
                text.append("You take a whiff... smells safe to drink " +
                        "(you stumble around)");
                break;
            }
            case 31855:
            {//slippery snake skins in possible gravy
                text.append("You take a whiff... it smells suspicious. " +
                        "Another whiff reveals that it smells VERY off " +
                        "spawns an aggressive level 90 snakeling that can venom. Exercise caution)");
                break;
            }
            case 31856:
            {//crystal clear water
                text.append("You take a whiff... smells safe to drink " +
                        "(antipoison)");
                break;
            }
            case 31857:
            {//underground passteurised milk
                text.append("You take a whiff... something smells VERY off " +
                        "(deals 66% your hp)");
                break;
            }
            case 31858:
            {//delicate elven wine
                text.append("You take a whiff... your head spins " +
                        "(takes you to Isafdar. " +
                        "You sense that your boat would be nearby");
                break;
            }
            case 31859:
            {//sea spray
                text.append("You take a whiff... smells safe to drink " +
                        "(screen sway for 20 seconds. Hurk!");
                break;
            }
            case 31860:
            {//banker's draught
                text.append("You take a whiff... it smells like the inside of a bank " +
                        "(banks everything. UIMs take 10% their hp instead)");
                break;
            }
            case 31861:
            {//headless unicornman's heady beer
                text.append("You take a whiff... smells safe to drink " +
                        "(fills the inventory with ensouled heads");
                break;
            }
            case 31862:
            {//soul juice
                text.append("You take a whiff... You take another whiff... " +
                        "a sense of deja vu washes over you, and you feel extremely uneasy " +
                        "(This is a two-part drink - if you've sampled a Bottle of Soul, " +
                        "this will deal 90% of your hp!)");
                break;
            }
            case 31863:
            {//crocodile tears
                text.append("You take a whiff... something smells off " +
                        "(deals up to 4 damage 4 times)");
                break;
            }
            case 31864:
            {//fishier bladderier stout
                text.append("You take a whiff... it smells VERY suspicious " +
                        "(takes you to the troll arena to fight 3 " +
                        "VERY dangerous trolls. Exercise caution)");
                break;
            }
            case 31865:
            {//Daddy's special water's special water
                text.append("<col=ff0000>When consumed: </col>");
                text.append("+100 sailing xp");
                break;
            }
            case 31866:
            {//The melted rocks
                text.append("<col=ff0000>When consumed: </col>");
                text.append("+100 sailing xp");
                break;
            }
            case 31867:
            {//Chuck up's 'stew'
                text.append("<col=ff0000>When consumed: </col>");
                text.append("+100 sailing xp");
                break;
            }
            case 31871:
            {//tangled toad's legs cider
                text.append("You take a ribbit... smells safe to ribbit " +
                        "(ribbit 15 ribbit)");
                break;
            }
            case 31872:
            {//Waterbirth blue lagoon
                text.append("You take a whiff... it smells suspicious " +
                        "(spawns a level 100 blue dagannoth)");
                break;
            }
            case 31873:
            {//fishtongue tonic
                text.append("You take a whiff... smells safe to drink " +
                        "(speak in fish for one minute)");
                break;
            }
            case 31874:
            {//endless night
                text.append("You take a whiff... smells safe to drink " +
                        "(reduces your kingdom of miscellania approval. Oops)");
                break;
            }
            case 31875:
            {//exile's welcome
                text.append("You take a whiff... your head spins " +
                        "(takes you to a safe lunar teleport. " +
                        "You sense that your boat can be found at Lunar Isle)");
                break;
            }
            case 31876:
            {//suquah-free cola

                text.append("You take a whiff... it smells amazing " +
                        "(full restore hp)");

                break;
            }
            case 31877:
            {//winter sun
                text.append("You take a whiff... your head spins " +
                        "(takes you to the desert. " +
                        "You sense that your boat can be found at Tempoross)");
                break;
            }
            case 31878:
            {//black lobster ale
                text.append("You take a whiff... smells safe to drink " +
                        "(lobter.)");
                break;
            }
            case 31881:
            {//dwarven wizard's stout bomb
                text.append("You take a whiff... smells safe to drink " +
                        "(fully drains melee stats)");
                break;
            }
            case 31882:
            {//KGP standard issue martini
                text.append("You take a whiff... your head spins " +
                        "(takes you to Rellekka Hunter area. " +
                        "You sense that your boat can be found at Rellekka. " +
                        "Unless Fremennik Trials hasn't been completed, " +
                        "in which case Port Sarim instead)");
                break;
            }
            case 31883:
            {//graveyard corpse reviver
                text.append("You take a whiff... it smells suspicious " +
                        "(spawns an aggressive level 103 which revives several times. " +
                        "Best to drink at a dock and let the boat air out)");
                break;
            }
            case 31884:
            {//Weiss meltwater
                text.append("You take a whiff... something smells off " +
                        "(deals 33% your hp)");
                break;
            }
            case 31885:
            {//ol' random's reddest rum
                text.append("You take a whiff... smells safe to drink " +
                        "(transform into an item)");
                break;
            }
            case 31886:
            {//Elidinis's life water
                text.append("You take a whiff... it smells amazing " +
                        "(full heal, cures disease, antipoison)");
                break;
            }
            case 31887:
            {//possible albumen
                text.append("You take a whiff... it smells suspicious " +
                        "(spawns an aggressive level 81 tortugan)");
                break;
            }
            case 31888:
            {//alone at sea
                text.append("You take a whiff... smells like peace of mind " +
                        "(park somewhere safe, and enjoy tranquility)");
                break;
            }
            case 31889:
            {//alco-sol
                text.append("You take a whiff... smells safe to drink " +
                        "(be prepared to be flashbanged)");
                break;
            }
            case 31890:
            {//portal nexus perry
                text.append("You take a whiff... your head spins " +
                        "takes you to the safe center of the abyss " +
                        "(You sense that your boat can be found at Port Sarim)");
                break;
            }
            case 31891:
            {//self-congratulation wine
                text.append("You take a whiff... smells safe to drink " +
                        "(woo, gz!)");
                break;
            }
            case 31892:
            {//fish bladder stoutier
                text.append("You take a whiff... it smells VERY suspicious " +
                        "(spawns Dinky again, but this time he's got hands! " +
                        "Can hit 50s, exercise caution)");
                break;
            }
            case 31893:
            {//crystal clear vodka
                text.append("You take a whiff... smells safe to drink " +
                        "(but not for your ears! Highly recommend turning down volume");
                break;
            }
            case 31894:
            {//spinning comp-kvass
                text.append("You take a whiff... smells safe to drink " +
                        "(gives a medium clue)");
                break;
            }
            case 31895:
            {//puzzlers poteen
                text.append("You take a whiff... smells safe to drink - Unless you answer incorrectly " +
                        "(5 quiz master, right answer give you a mystery box, " +
                        "wrong answer deals 10% hp)");
                break;
            }
            case 31896:
            {//wild wood whisky
                text.append("You take a whiff... it smells suspicious - but in a good way " +
                        "(spawns friendly critters)");
                break;
            }
            case 31897:
            {//Robert's Port
                text.append("You take a whiff... something smells off " +
                        "(deals 10% hp 5 times. cooked fish reduces damage");
                break;
            }
            case 31898:
            {//Sea Shandy 2
                text.append("You take a whiff... your head spins " +
                        "(takes you to port sarim. " +
                        "You sense that your boat can be found nearby)");
                break;
            }
            case 31899:
            {//monkfish bladder stout
                text.append("You take a whiff... it smells suspicious " +
                        "(spawns an aggressive level 217 drink troll queen)");
                break;
            }
            case 31900:
            {//lunarshine
                text.append("You take a whiff... smells safe to drink " +
                        "(enjoy the new 'fro)");
                break;
            }
            case 31901:
            {//impling surprise
                text.append("You take a whiff... smells safe to drink " +
                        "(gives a random impling jar)");
                break;
            }
            case 31902:
            {//mystery fruit cider
                text.append("You take a whiff... smells safe to drink " +
                        "(you're not sure what this does)");
                break;
            }
            case 31903:
            {//Mystery fruit
                text.append("<col=ff0000>When consumed: </col>" +
                        "transforms you into a fruit");
                break;
            }
            case 31905:
            {//Captain Cat's black rum
                text.append("You take a whiff... it smells suspicious - but in a good way " +
                        "(spawns sailor cat)");
                break;
            }
            //endregion charting drinks
            default:
            {
                Effect effect = service.getItemStatChanges(itemId);
                if (effect == null) return null;
                StatChange[] stats = effect.calculate(client).getStatChanges();
                text.append("<col=ff0000>When consumed: </col>");
                for (StatChange stat:stats)
                {
                    text.append(stat.getStat().getName()).append(" ");
                    text.append(stat.getFormattedTheoretical()).append(" ");
                }
                break;
            }
        }
        if (text.length() == 0)
            return null;
        else return text.toString();
    }
}
