/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import prpobjects.Guid;
import moulserver.Node.*;
import shared.*;

public class NodeUtils
{
    public static SystemNode GetOrCreateSystemNode()
    {
        SystemNode result = SystemNode.findIfExists();

        if(result==null)
        {
            //Add SystemNode and Global Inbox, which should be shared among all users, but let's just make it per-user for now.
            SystemNode systemnode = new SystemNode();
                FolderNode globalinbox = new FolderNode(FolderType.kGlobalInboxFolder);
                    //Add MemorialImager folder, which we've subscribed to, I guess.
                    FolderNode memorialimagerfolder = new FolderNode(FolderType.kUserDefinedNode, "MemorialImager");
                        TextNoteNode memorialimagertext = new TextNoteNode("MemorialImager","Janet \"Pehpsi\" Burress\r\nCAGrayWolf\r\nJames \"Aquila\" Carpenter\r\nWillow \"Wheely\" Engberg\r\nGandhar\r\nPhil \"phend\" Henderson\r\nJahuti\r\nJDrake\r\nKatzi\r\nMo'zie\r\nRamsine\r\nJim \"Dust'ei\" Rhodes\r\nRosette Taylor\r\n");
                        memorialimagerfolder.addChild(memorialimagertext);
                    globalinbox.addChild(memorialimagerfolder);
                    //Add journals folder
                    FolderNode journalsfolder = new FolderNode(FolderType.kUserDefinedNode, "Journals");
                        TextNoteNode sharperjournaltext = new TextNoteNode("Sharper","<cover src=\"xSharperJournalCover*1#0.hsm\"><font size=18 face=Sharper color=982A2A><margin left=62 right=62 top=48>11.14.97 - Looks like they've agreed to let me take \"control\" of Teledahn. Time to start a journal. Officially. \r\n\r\n11.17.97 - Maybe not. Kodama \"popped in\", going on about his inspections, in his usual arrogant manner. What a joke.\r\n\r\n11.24.97 - Now it's Watson's turn. Acted as though he was chatting but I could tell he was looking all over, checking on my progress, or maybe making sure I can be trusted. I'll just get used to it. \r\n\r\n11.25.97 - Time to move forward. DRC isn't going to change anytime soon.\r\n\r\n12.15.97 - Merry Christmas. Going up for a few months. Can't take this red tape anymore.\r\n\r\n1.29.98 - Yay for Broncos. Patriots should have been there. Stupid Steelers. Okay, maybe Teledahn will help me to forget all this. \r\n\r\n<font color=000000>2.15.98 - Looks like I'm going to need Watson after all. I've found all kinds of journals, and notes upstairs that I'm going to need translated. I think Watson is going to let one of his assistants help me out. Sam.\r\n\r\n3.1.98 - Sam is not the fastest translator I've ever seen. I don't think he's even started. Kodama came by again today. \r\n\r\n3.3.98 - Spotted something today. Creature of some kind. Forget the history of this place, for now. I've got to see that thing again. \r\n\r\n3.7.98 - Saw her again. Wow. What a beauty. \r\n\r\n<font color=323853>3.9.98 - She's very sensitive to sound. Startles like an antelope. I'm estimating she's a good forty feet.  Killer whale type. Hard shell though. \r\n\r\n3.18.98 - No sign of her in this area, at least. Sam said he's going to have some time next week. At this rate...\r\n\r\n<font color=982A2A>3.25.98 - She showed up again. I saw her eating. She likes the flappers. Feeds on them. Pretty quick and agile for her size. Surprised me. Of course, those flappers aren't real bright. Those who weren't eaten went right back to the spot and waited for her to show up again. \r\n\r\nLooks like this place was written in 8990 for a D'ni Lord. Guild of Caterers. 250th birthday present. If I'm ever 250, someone better give me something better than this place. \r\n\r\n4.5.98 - Sam is busy again. Did get me some more translations though. Seems like the mushrooms were used for some kind of delicacy. To be honest, I'm not sure Sam got that one right. Doesn't make a lot of sense, not with what I'm seeing. \r\n\r\n4.7.98 - Watson told me Sam is too busy to help me. I'm going to have to learn this language myself, or find someone who can actually help me. \r\n\r\n4.8.98 - Watched her for a while today. Definitely feeds on the flappers close to shore. Also feeds on mushrooms. \r\n\r\n<font color=323853>4.15.98 - Does she ever feed on mushrooms. Watched her completely destroy one today. Brought the whole thing down and fed for some time. Until scared off by something. \r\n\r\n4.17.98 - Mushroom is gone. Probably sank. Kodama came by again today and I was glad the girl wasn't around. Last thing I need. \r\n\r\n5.14.98 - 1. The flappers like the spores. 2. The creature likes the Flappers. 3. She's scared to death of loud sounds. If I get this equipment running, she's gone. Heading up in a week. Going to try and get this gate down before I go. See if she'll come in while I'm gone. \r\n\r\n8.12.98 - Three mushrooms were down. Seems all of them were a particular kind. She was in the lagoon. I think I could have taken her out, but not yet. DRC would have a fit. Probably kick me out or something. She's definitely an air breather. Could hear her today. Sleeping, on the surface. Kodama followed me here and scared the heck out of her. She woke and shot out of here, fast. Kodama never saw her. Apparently some new guy is learning D'ni and wants to work with me. We'll see. \r\n\r\n8.28.98 - There are quite a few new people coming down. A group from some game company was recently here and there was quite a stir. I met a few of them. Nice guys. \r\n\r\n9.15.98 - Haven't seen her for a full month. I'm going to start working on the equipment here. Can't wait forever and the DRC is getting on me. As though they owned the place. \r\n\r\n<font color=000000>10.1.98 -  The tower is almost working. Need some tools from the surface. No sign of Shroomie. Met the new guy - Nick - nice guy. This might be real good. Smart guy. Picking up D'ni fast. He's going to keep at it, but I gave him some material to study in the meantime. \r\n\r\n11.5.98 - Quick trip up and back down. Tower is working. Nick dropped off some translation and it all matches the old stuff. He's working on new material now. Tower is powered and more projects. \r\n\r\n11.14.98 - These buckets are a mess. So is the elevator. Have found some kind of pump mechanism to get that water out. I think all of it will have to wait. I'm heading back up for holidays and end of season. Tickets to Monday night game against Miami. Patriots still in the playoff hunt. \r\n\r\n1.7.99 - Patriots out. Back down. Nick is more than I could have hoped for. Seems up for keeping the translation out of DRC's hands. Has had some bad experiences with Kodama and Engberg and in my camp now. Perfect. \r\n\r\nApparently Hinahsh only owned the Age for ten years. At his death, Teledahn was left to the Guild of Caterers who installed the equipment that I'm working on today. Some of the translation has actually been rather helpful. I think I can get the pump working. Was never meant for water, but I think it would pump out the water. \r\n\r\nNick says there is quite a bit about a Guild Captain Ventus who ran the Age for quite a few years and directed the industrialization of the place. Did quite a job apparently. Although, Nick is still reading. \r\n\r\nSigns of Shroomie, but I haven't seen her for a long time and I'm not going to stop working on the equipment now. Apparently no mention of her in the stuff Nick is reading. \r\n\r\n2.4.99 - Pump works, although I'm keeping the water there. Nice form of protection to the other side. At least until I discover what it was used for. That's Nick's job now. I have my ideas though. \r\n\r\nSeems as though Ventus installed the gate to keep Shroomie out of here. Didn't like her eating his mushrooms. Good idea. \r\n\r\n2.6.99 - Ventus maybe wasn't so great after all. Ruined the place. Turned it into what we see now. Explains the differences in early descriptions to the later ones. \r\n\r\nSeems the Age was auctioned off and that's all of the official records. Nick can share those with the DRC. I don't mind. \r\n\r\n2.8.99 - Showed Nick some of the manuscripts I've kept \"hidden\". I'm quite sure I can trust him. He's given the official report to Watson and the others and is willing to do these extra translations on the side. Good man. \r\n\r\n3.1.99 - Watson, Kodama, and Sutherland came by today. It was pre-arranged so they didn't see anything they didn't need to. They seem satisfied with the work I'm doing, although the fact they continue to check on me still drives me mad. They say they want the Age ready for visitors relatively soon. I didn't realize that anyone and everyone would be allowed access to the place but why not I suppose. I'll still have my areas. \r\n\r\n<font color=323853>3.4.99 - Big argument today with Watson. Upset I didn't share with him all that Nick had translated. Mis-communication although I'm happy Nick has kept his mouth closed regarding the other. Regardless, I can't take their nit-picking. I'm heading to the surface for a long trip. Returning to Africa again with the fellows. Don't know when I'll be back here. \r\n\r\nNick knows to keep things quiet. I've set things up for Shroomie to return and I have plans to bring down some new items. Look forward to returning a long time from now. \r\n\r\n2.15.00 - Back to Teledahn again. The surface trip triggered some ideas for here that I think I'll begin exploring. Some talks with Engberg might be in order soon. \r\n\r\nNick has gotten a load of translation done. I'm going to try and summarize as best I can. If I can remember everything. \r\n\r\nThis place was owned by a fellow named Manesmo. The man apparently got the place cheap somewhere. He started the harvesting of spores again - it seems the Age had corrected itself over time. Bread apparently, they were making. The same delicacy D'ni had raved about before. Made a decent amount of money. \r\n\r\nHowever, he was doing lots of stuff in the dark. Slave trading. I'm sure Watson would have a fit if he knew this. His precious D'ni. Where they were going, we can't find out. But it explains the cages and the whole backside of this place, really. Hiding from Maintainers I suppose. \r\n\r\nWe found some more mention of Shroomie as well. Manesmo saw her pretty frequently. They even found her nest. I'm heading out tomorrow to see. \r\n\r\nAs far as Shroomie herself, she has been here, but was not here when I arrived. Shrooms have managed to disappear and she broke a walkway on the backside. Have to fix that now. \r\n\r\n2.17.00 - No nest. Remains, yes, but she's obviously not been there for a long time. This place is much bigger than I thought. Learned that today. \r\n\r\n<font color=000000>3.3.00 - The DRC is getting pretty serious about letting people down here. They have moved to The Island and are trying to get portions of the city open for visitors. Moving headquarters to a building there as well. I suppose I'm going to have to get serious about it as well. They're going to make me if I don't. So back to the equipment. \r\n\r\n4.15.00 - Cars are giving me all kinds of trouble. So is this ridiculous elevator and  I don't know why. I'm making a quick trip to the surface. Parts. \r\n\r\n5.23.00 - Back with parts. Nick has found out that slaves were going to a place called Rebek. Haven't heard of it myself. I'm going to ask Watson tomorrow. \r\n\r\n5.25.00 - Watson has heard of it. Says they've been there. Asked how I knew and I realized it was a mistake to ask about it. Told him Nick told me and fortunately Nick told me later he had been doing some official translation for the Age. Lucky me. Be more careful. \r\n\r\n5.28.00 - Found a new book today. A very special book. \r\n\r\n6.15.00 - Cars are working. Why they need to work I don't know but apparently the DRC wants this place restored to its original condition. So, cars are working. \r\n\r\n7.2.00 - Elevators work. Finally. Nick tells me this Rebek Age was amazing but he was pulled off of it. Games with the slaves from here were played there. Hunting game of sorts. I'm not for hunting people but the game does sound fun. Doubt Watson will want to approve that Age too fast. \r\n\r\n8.2.00 - DRC is planning on opening this place up in 2002. They, of course, haven't bothered to tell me that but regardless, it's true. Working on getting some lights going in here. I have a feeling inspections will be increasing. \r\n\r\n8.10.00 - Watson informed me of the plans today. 2002 is the target. Thanks.\r\n\r\n8.12.00 - Nick stopped by and we've got a little more information. Looks like there is some kind of weapon in our hands. At least it could be used as a weapon. After the lights. \r\n\r\n8.15.00 - Inspection planned for next month. \r\n\r\n9.12.00 - Lights are functioning. Cars. Doors. Elevators. Not sure what else they'll want but I'm sure they'll come up with something. \r\n\r\n9.15.00 - Well, stupid me. I have an entire list of items that need to be accomplished for this place to be \"safe\". I won't be taking any more Ages, after this one. Maybe a city location. I can't take this. \r\n\r\n9.17.00 - Shroomie is back. Watched her all day. Out of the blue, I think she's starting to like me. This could be good. I'll give up working for a short while. \r\n\r\n9.20.00 - She's nervous but coming back daily. I keep the gate down. \r\n\r\n9.21.00 - She's trapped. Got the gate up with her inside eating flappers. Set her off. Pretty obvious she can be a nasty girl if she wants. But still I'll have a shot. And I need to do it before Kodama shows up. \r\n\r\n9.22.00 - Got her. Time for a surface trip with the important parts. I'll sink the rest.\r\n\r\n10.30.00 - Wooden walkways are fixed. Rails are up. Among the other things on the list of DRC requests. If this place isn't safe, I don't know what is. Inspection tomorrow. \r\n\r\nNick has dropped off some more translations. Seems as though a major inspection into the illegal activities was going on immediately before the fall.  Doesn't look like they found anything. \r\n\r\n<font color=323853>11.1.00 - Inspection went fine. I guess this place is safe now. I'm heading back to the surface for more tools, football, and the holidays. \r\n\r\n<font color=000000>2.1.01 - Another missed playoffs. Oh well. Things are stressful here. One year to go for initial visitors and it is beginning to show. However, Sutherland dropped by today. Nice woman. We had a nice talk. \r\n\r\n2.5.01 - I'm helping out in the city now, not much work to do in Teledahn. Clean-up here and there. Maintain. I'm hoping for a certain location in the city, maybe my helping will get me some leverage with Watson. I'm enjoying the time with Sutherland anyway. \r\n\r\n3.3.01 - More city work. Not much happening here. \r\n\r\n4.7.01 - Nick managed to get me an extra Teledahn book today. Good man. \r\n\r\n5.12.01 - Shroomie is back! Obviously not the same one, but we've got a new girl here. Amazing. I'm curious to know how many there are now. I think I'm going to try and schedule another trip in the next couple of months. \r\n\r\n5.23.01 - The trip was a success. Amazing. There are quite a few of these creatures all over. Perhaps a seasonal thing. Waters where I had been before I found a pod of the creatures. And a bigger one. I'm not sure but these seem to be young ones. The larger creature was absolutely stunning. I've never seen anything like it in all my days of hunting. I was actually a little frightened. This thing could have swallowed my boat whole. To take even a small portion home would be...I'm going to have to think this one through. \r\n\r\n6.1.01 - Back to work here. I'm installing a gun of some kind down on the docks. It's a D'ni mining instrument I believe. Regardless, the DRC, of all people, want it set up. Strange. \r\n\r\n6.30.01 - Gun is up. Not working, but up. Laxman will have to get it working, or at least give it a shot. Not familiar enough with this kind of D'ni technology. \r\n\r\n7.5.01 - More city work and less Teledahn work. Did clean up the cages and the larger mushroom. Took out some crates and moved them upstairs. I think another surface trip is in order. Marie wants to go up as well. \r\n\r\n10.12.01 - A little longer than I had expected for obvious reasons. Horrible tragedy. I'm happy to have D'ni. A distraction of sorts. \r\n\r\n10.14.01 - I brought down a fish tank. At least the first part. Want to see if I can learn more about these flappers. I can get some young ones and some water from the lake. We'll see.  \r\n\r\n10.21.01 - The stress level is rising rapidly. Only a few months out and they are planning on bringing down some visitors. They are cleaning up a neighborhood for a group of new people. Interesting. \r\n\r\n11.2.01 - Victor found some kind of communication device this week. A KI they are calling it. Very interesting device, I have to admit. Victor does seem to know his stuff, at least. Talked to Engberg about a building in the city that I'd like to have. We'll see. \r\n\r\n11.3.01 - Victor can't get to the gun for a long time. In fact, DRC wants the whole thing taken out now. Fine by me. \r\n\r\n11.5.01 - Found another new book today. Perhaps I have my city location now. Great view as well. I'm moving some things there. A little more out of the way. Glad I bought the more expensive fish tank now. \r\n\r\n11.22.01 - Received a report from the surface. Patriots aren't doing well and I don't think I'm going home this holiday. I'll help here. Teledahn seems stable but Kodama has asked me to look at a different Age and help them out. Sounds like an interesting place. Ahnonay or something. Why not? \r\n\r\n12.12.01 - Strange place. Needs lots of work but it's been good. I think I'm heading up to the surface. Watson had recommended we go up. Marie is joining me again. \r\n\r\n2.10.02 - Wasn't supposed to be gone this long but who would have thought the Patriots would win the Super Bowl! I don't think Watson is very pleased but too bad. Visitors are down and I wasn't here to see them. \r\n\r\n2.16.02 - The DRC wants me to go through Teledahn again, although I'm not sure why. This place has been cleaned up for months. Typical of them. I should know better by now. \r\n\r\n2.17.02 - New Shroomie creature seems pretty happy here. Runs when the machinery comes on but usually returns. I usually leave the gate down. \r\n\r\n2.23.02 - KI's are working great. Pretty amazing devices. Haven't met any new visitors. Watson says he doesn't want me to. Typical bureaucratic nonsense. \r\n\r\n3.1.02 - They are ready to open up The Island. I have to admit I'm pretty excited about watching these people visit. I'd love for them to come to Teledahn, but the DRC insists it's not ready. Not sure what more they want. \r\n\r\n3.20.02 - A new group of visitors are coming. The DRC is really hyping this one. Authorized explorers. \r\n\r\n3.27.02 - Enjoyed talking to Robyn and Rand again. Nice guys. Hadn't seen them in a long time. The new authorized explorers seem to be enjoying themselves. Fun. \r\n\r\n4.5.02 - Funny. DRC posts that restoration efforts will be given top priority this month. More meetings and more inspections when the Age has been ready for months. If it makes them feel better. They did acknowledge that I found the book. Wasn't expecting that. \r\n\r\nOn an even funnier note, all meetings are being held in the Tokotah now. I couldn't have asked for much more. \r\n\r\n<font color=323853>4.20.02 - So they made that public! Shroomie was in here and I had to make up some reason to postpone the trip. If they had seen the equipment and such, in fact, it's time to get rid of some of this. If the DRC had come here today, could have been bad. \r\n\r\n4.25.02 - Phase Five approval. What's that even mean? \r\n\r\n5.12.02 - Simpson told me about a pretty strange stone, so I tried it out. I want it myself. I think I'll keep it here. Don't really care what the DRC thinks of that either. \r\n\r\n5.17.02 - Stone is gone. Vanished right out of my office. Where's Simpson?\r\n\r\n5.20.02 - Got the stone back again. Simpson claims it was back where he found it. Regardless, this time I'm keeping it in a more secure location. \r\n\r\n5.25.02 - Now Phase Five approval. I've been so angry this past month, I've been ready to throw in the towel and head back up. It's utterly ridiculous what they are doing. The safety requirements they are pulling out. Somebody had better do something before we have an all out government down here. Although these threads on the forums may be something. The DRC, in secret, is suspecting Zandi. This could get fun. \r\n\r\nStone is gone again. How?\r\n\r\n5.30.02 - Last time with the stone. I'm trying a more secure location.\r\n\r\n6.3.02 - Gone again.\r\n\r\n6.7.02 - I have the stone one more time. Simpson says he can't take it again. So I'm trying one more thing. Gut feeling about these creatures.\r\n\r\n6.18.02 - Watching people here has been fun. Maybe it all has been worth it.\r\n\r\n6.20.02 - Stone hasn't gone anywhere this time. I think they're afraid of the hanging rocks. Interesting.\r\n\r\n<font color=000000>7.11.02 - Surprise, Rebek was shelved. I saw this one months ago. \r\n\r\n7.23.02 - This is wild. They are flipping out over there with these Zandi breaches. Hilarious. I love it. Of course they haven't mentioned a thing to me yet. I'm sure they don't plan on it. \r\n\r\n<font color=323853>8.26.02 - Big meeting this weekend for the DRC. They don't know what to do about Zandi and it's driving them nuts. They think it's only going to get worse. I hope so. \r\n\r\n9.3.02 - Kodama wants to bet on surface games now? Who would have thought but I'll take his money. \r\n\r\nOn a more adventurous note, Nick got us into Rebek today. Amazing. I'd love to spend some time there but we had to hurry. He tells me there is a new Age as well with creatures. I'd love to get there but I doubt it will happen. The DRC is sick to their stomach with this Zandi stuff. I'm on his side but I hope this just doesn't make things worse. \r\n\r\n9.07.02 - Zandi is getting them on his side. I wish I could describe Kodama. Wow. I love it. I did see his sign on the surface. No one ever seemed to notice it though. I guess he's making them. Ha. \r\n\r\n9.14.02 - Marie has lost it. A t-shirt? I agree with Kodama on that one. \r\n\r\n10.14.02 - Kodama is taking money from me and it's driving me crazy. If I wasn't watching his veins burst in those meetings, I'd be much more angry. It's worth it though. \r\n\r\n11.14.02 - Well Zandi is unstoppable now. He's going to bring people down and they can't stop him. It is funny but it's also more work. I want this to work for everyone so I'm helping out more where I can. Still hoping for leverage on that structure I've been wanting for a few years now anyway.\r\n\r\n<font color=000000>11.18.02 - This Zandi stuff is great. I do think the DRC is going to be ready for them but still, more power to him. Amazingly, no one has officially talked to me yet from the DRC. \r\n\r\n12.01.02 - Looks like Uru is it now. Thanks to Zandi, again. \r\n\r\n12.24.02 - Christmas Eve in Teledahn. If I imagine hard enough the spores look like a Northeast Christmas. Not really. I think I'm heading back for the playoffs and New Year. \r\n\r\n1.05.03 - No playoffs but still a good New Year. This should be a fun year for D'ni. Also brought down some more fish tank pieces this trip. I think it's about ready. If I get a chance to work on it.\r\n\r\n1.07.03 - Should be interesting to watch Zandi and the DRC as well. I'll be anxious to see who gets the power here. \r\n\r\n2.01.03 - Looks like they are trying to get the upper hand. Explain the Zandi story and be up front about it. Watson had to convince some of his members of that one believe me. But probably smart. \r\n\r\n<font color=323853>3.15.03 - Ages are being approved, visitors are coming down. I'm losing time to write journals. \r\n\r\n4.2.03 - Closing, opening. I don't understand the idea but the DRC is definitely trying to get ready for the new arrivals. Another large group just came down and it seems as though things are going fairly smooth. \r\n\r\n5.4.03 - I've heard them talking recently about a house out on the Island. They are beginning initial restoration but I've got to find a way on that. Nick says the place is amazing. He's read some histories and the stories go on and on and way back. I'm going to talk to Watson now. \r\n\r\n5.20.03 - There are rumors of a D'ni survivor going around here. The DRC is keeping it lo-key but it seems pretty reliable. Obviously, I'll be the last to hear but I've got to find a way to meet him  as soon as possible.\r\n\r\n6.25.03 - Funny. Seems like a lot less people are in the city nowadays. DRC is talking quite a bit about visitors' access to Ages that they have not approved. They are a little irritated to say the least, and trying to figure out a way they can control it. I don't think they can. Interesting. \r\n\r\nFinally got the gun up and working. Laxman is getting better every day. The guy is a genius. Watson stopped by as well to see it working although he seemed more concerned with me cleaning up the scrap than the gun itself. Typical DRC.\r\n\r\n7.18.03 - Out of the blue, Sonya Michaels has contacted me. Old friend from the surface working for some paper up in Maine. Says she is coming down for a long period. Wants to write.\r\n\r\nOn a more typical note, Watson came by a few days ago asking me to name the wildlife here. They aren't finding the names the D'ni used for a lot of the animals. I don't think he really appreciated \"Shroomie\" and \"Flappers\". Anyway, I named the birds here \"Buggaros\". I think some part of that is D'ni for big - that's what Nick said at least. Big bug. Wonder how they'll like that?\r\n\r\n<font color=982A2A>9.22.03 - Just returned from a short surface trip.  Went to last week's Patriots game in Philadelphia. Excellent game. What they needed after that miserable performance in first week. And another win yesterday. Travis managed to meet me in Philadelphia and brought little Travis down with him. It was good to see them both. \r\n\r\nSonya seems to like it down here. In fact, it looks as though she'll be staying for a while - longer than she had thought - and may be working for the DRC in a similar role as Simpson. Fine with me. Another insdie scoop never hurts. \r\n\r\nI'm going to try and get a pub down here. DRC has apparently made long-term plans to open up a new section of the city and there is a spot I'd love to have as my own. Time to start being nice again and see if I can get it. \r\n\r\n<font color=000000>10.29.03 - It looks like this is actually going to happen. Major preparations are underway to open the place up. The arguments for not doing it have been pushed under for now, and it's going to happen. When the hordes come, I've got to get out and meet them - talk to them. It's an opportunity to establish myself I won't have later.\r\n\r\nThe pub is looking good. Very good. DRC has no idea the Books were ever there (good work Nick) and it looks as though they are going to let me have it. I'd love to turn it into something to show off my favorite trophies. Wouldn't be bad to open it up for others as well. Have to think about that more. \r\n\r\nCan't get to a game right now and it's killing me. Too much going on down here. But, I will be there for the playoffs.  Talked to Travis today and we're going, wherever it is, although at this point I'm planning on Boston.\r\n\r\n10.30.03 - It's official. The pub in J'Taeri is mine. Time to celebrate. Time to prove Kodama wrong.\r\n\r\n11.24.03 - People are coming down from all over and I have to say they are good people. Smart people. See right through the DRC and their games. At least most of them do. Definitely will be sides at some point. A few of the visitors in particular have impressed me. Dusante. Tweek. Lucas, and others. Need to start writing down names. Don't want to forget them.\r\n\r\nEngberg has been odd lately. I've heard it from many of the visitors and seen it myself as well. Pressure must be getting to him. Little does he know, it's only making the DRC look worse. \r\n\r\nPatriots win again. No surprise anymore. Bad time to be stuck down here but these are critical times. Can't go to the surface now. \r\n\r\n11.25.03 - This could be going very well. Spoke with a Brian Fioca and Duskin D'tahree (strange name) tonight. I could see them as eventual leaders. \r\n\r\nEngberg is whining again. Not good. They've called a meeting. This better not mean the pub is gone.   \r\n\r\n11.26.03 - Excellent talk with Brian tonight. Good man.  \r\n\r\n11.28.03 - What is it with this Phil character? The man is disturbing, yet intriguing. I was able to talk to him last night for a while in Kemo. He's probably just crazy but if they will listen to him, he could be very useful. More people need to hear him. He's agreed to meet with me again. This could be just what I needed. \r\n\r\n12.1.03 - Sounds like it may have been one of the best games all year. I may not have a choice but to make a surface trip for the Jaguar game on my birthday. Have to see how things are progressing down here. \r\n\r\nDRC is obviously aware of Phil, as they should be. I have to say I'm happy to have the distraction. I haven't heard anything about Engberg's call for a meeting recently. Good timing. \r\n\r\n12.8.03 - Phil. What perfect timing. The things he showed me this weekend are beyond anything I could have expected. I may have bought into the DRC propaganda myself. Not again. The DRC knows so little of the power of D'ni. It's almost funny. Yeesha is the key. With her, there is strength. It's either Yeesha or the DRC. That needs to be the message and Phil will help me in speaking it. They will listen to him.\r\n\r\n12.15.03 - Didn't think I would be able to forget about what was happening down here, but sitting in the snow, watching Poole run that interception back. This team is as good as I've seen them. I can't wait for the playoffs. \r\n\r\nBeing back on the surface cemented my decision. I'll be coming back for Christmas. In the meantime, I think I'm going to cut down a tree on my Relto and decorate it for the office here. \r\n\r\nNeed to find Phil today. Haven't met with him in a while and that's never good, even if the game was worth it.  \r\n\r\n12.15.03 - They've gone too far this time. The nerve of these people. The war has started. They can not simply take Phil away with no reason and not expect consequences.\r\n\r\n12.19.03 - I have given them consequences and now it seems they will give me a taste of my own medicine. Kodama seems to have started the ball rolling. I fully expected this sort of reaction. I just hope I have enough people on my side when I need them. \r\n\r\n12.28.03 - What a holiday. Though things down here are a complete mess, at least the surface is going well. Patriots won again. Was able to spend time with Megan, Sean, and Brian - took him to the game, although down here it seems as though my credibility and respect has taken a downturn. \r\n\r\nPhil has apparently been killed, or at least that is what the DRC is saying. I'm not sure whether to believe them or not, but the evidence is not good. I've seen the pile myself. Watson has left for the surface, too upset to stay. Laxman and Kodama are taking a role. The city has been closed again. I'm losing my fight for this whole mess. It's too much and the people here seem to care little. It might not be worth it. \r\n\r\nPlayoffs in two weeks. At least I have that to look forward to. \r\n\r\n12.31.03 - It has been quite a year, especially recently. I have to say I never planned things to go this way, although looking back it may be for the best. Watson has left. Somewhere. Laxman and Kodama seem to be running things which, for the moment, is definitely good. At least they are moving somewhere. Though I may not always agree with them, I like their drive. I'm thinking about giving it a little more time and seeing if I can't get my pub back. \r\n\r\nI've been keeping quiet lately, on purpose, letting things run their course. I may have become a little to aggressive and I don't think I was helping myself. We'll see if I can't do things better this year. \r\n\r\nStill planning on heading up to the surface next week. Make the playoff trip a little longer than just the weekend. \r\n\r\nThough this baby isn't quite full, it seems a good time to start a new journal. So this is it. My last entry for this book. 6 years in the making. Hard to believe.\r\n\r\n\r\n<font color=000000>3.7.07 - The smell of this place hasn't changed. It's as though I never left until Cate links in and says hello.\r\n\r\nWell... here we go again...\r\n\r\n3.9.07 - I've given up looking for the other journal - who knows who took it? Suppose I'll fill this up instead.\r\n\r\nNegilahn seems to be task one. The creature or thing, whatever it is there, eating everything else. I had no intention of hunting anymore down here and the first thing the DRC asks me to do is exactly that. Odd, but what can I do? I'm not going to turn down a job like this.\r\n\r\nApparently Nick has someone who is interested in helping out with Negilahn but he can't seem to remember the person's name. Not sure what's happening with the poor boy but he seems distracted... ever since I've come down. I only hope it's not a woman or something. We don't need that. He's going around asking everyone who this mysterious person is and of course everyone is volunteering. The poor boy never learns. He did just stop by again after another visit to the Great Tree's Bevin (wonderful name) and said it might be a guy named Rils. We'll have to contact him. Seems Nick had a note from him and his Zoological Society all along.\r\n\r\nThe boy is distracted but we'll get it done.\r\n\r\n3.12.07 - I suppose I had better give the boy credit. Nick tracked him down and seems to have an agreement with this Rils character. I'll determine for myself whether or not he's fit to accompagny us to Negilahn but from all accounts, it sounds like he is.\r\n\r\nHopefully he's a better shot than Nick. Judging by target practice yesterday, I'm not sure we'll have much hope if I'm unable to hold a gun for some reason. I need to remember to make sure Nick isn't sending off messages to all of his friends while we're tracking either.\r\n\r\nThe boy has got to learn to focus.\r\n\r\n3.14.07 - Rils seems fine. I was a bit nervous at first but anyone who does what he does can't be all that bad. The man has used a gun in the past but we'll see. I'd put him in the same level as Nick although I'll give credit to Nick. The boy has improved.\r\n\r\nEither way, if I go down, they'll be in trouble. I won't go down.\r\n\r\nIf I had buried the days of the past, they were dug up by a gentlemen named Vormaen. He brought up Phil and attempted to interrogate me on those events that happened years ago. The man claimed people are worried I'm going to start a war.\r\n\r\nIf there is a war to worry about, it's not one I'll be starting.\r\n\r\n3.19.07 - Surprised Boy Wonder with a trip to the surface to see his Ducks play in Spokane. Two games and two wins. Not to mention good times with Nick. Good kind.\r\n\r\nOf course, Nick forgot to mention the trip to Rils so I'm sure he was waiting the whole weekend for a Negilahn expedition.\r\n\r\nGood kid with a lack of focus.\r\n\r\n3.22.07 - Bad night of sleep. Nightmares. I've never seen anything like it in my life.\r\n\r\nCate wants my report in an hour. Still not exactly sure what kind of woman she is but I have to choose my words carefully. Closing the Age won't be good for anyone right now.\r\n\r\n3.23.07 - I can't say I'm surprised, but I didn't expect her reaction. Cate is not exactly what I thought.\r\n\r\nNegilahn will stay open. The pods, for some reason, this thing, or things, won't go near the pods.\r\n\r\nI have some theories I'll look into myself before another long trip with Rils and Nick.\r\n\r\nI have some theories.\r\n\r\n5.3.07 - I don't seem to have quite the same desire to journal I once did. Regardless, I figure I better write something down. It's been two weeks of studying the outsides of the pods and the number of deaths seems to have dropped quite a bit.\r\n\r\nI would normally think that good news, and I still do, but the overall decrease in population is going to eventually cause a drop of some kind. I think that's the primary reason.\r\n\r\nI have no doubt this is not a disease, not a sickness, or anything like that.\r\n\r\nIt's a predator and a very mean one at that. I have now seen glimpses of it.\r\n\r\nWhile writing this, Cate sent me a message. The boy has been fired from the DRC. I suppose all this involvement with Jazz, Heaven, and Sydney had to catch up to the poor boy at some point. I tried to warn him.\r\n\r\nI can't say I'm surprised. Too bad. Not sure what that means for him working with me but I don't think it's good.\r\n\r\nI better go find him and see what he plans to do now.");
                        journalsfolder.addChild(sharperjournaltext);
                    globalinbox.addChild(journalsfolder);
                systemnode.addChild(globalinbox);
            result = systemnode;
        }

        return result;
    }
    public static Node.PlayerNode CreatePlayer(String playerName, String avatarShape, Guid accountGuid)
    {
        //Both the MemorialImager and Journals folders were subscribed to, as were there entries, which were also in the GlobalInbox.

        //Create PlayerNode
        PlayerNode player = new Node.PlayerNode(playerName, avatarShape, accountGuid, 1);
        Node.getRoot().addChild(player); //add to root. perhaps this should be moved to another spot on the tree.

        //Create PlayerInfoNode.
        Node.PlayerInfoNode playerinfo = new Node.PlayerInfoNode(player.getIdx());
        player.addChild(playerinfo);

        //Add AgeNode for AvatarCustomization age.
        //Node.AgeNode avatarclosetnode = new Node.AgeNode(player, playerinfo, "AvatarCustomization", "AvatarCustomization", playerName+"'s", "AvatarCustomizationDescription");
        //player.addChild(avatarclosetnode);

        SystemNode systemnode = GetOrCreateSystemNode();
        player.addChild(systemnode);

        PlayerInfoListNode buddylist = new PlayerInfoListNode(FolderType.kBuddyListFolder);
        player.addChild(buddylist);
        AgeInfoListNode agesicanvisit = new AgeInfoListNode(FolderType.kAgesICanVisitFolder);
        player.addChild(agesicanvisit);
        FolderNode avatarcloset = new FolderNode(FolderType.kAvatarClosetFolder);
        player.addChild(avatarcloset);
        FolderNode agejournals  = new FolderNode(FolderType.kAgeJournalsFolder);
        player.addChild(agejournals);
        FolderNode chronicles = new FolderNode(FolderType.kChronicleFolder);
        player.addChild(chronicles);
        PlayerInfoListNode peopleiknowabout = new PlayerInfoListNode(FolderType.kPeopleIKnowAboutFolder);
        player.addChild(peopleiknowabout);
        FolderNode avataroutfit = new FolderNode(FolderType.kAvatarOutfitFolder);
        player.addChild(avataroutfit);
        PlayerInfoListNode ignore = new PlayerInfoListNode(FolderType.kIgnoreListFolder);
        player.addChild(ignore);
        FolderNode playerinvite = new FolderNode(FolderType.kPlayerInviteFolder);
        player.addChild(playerinvite);
        FolderNode inbox = new FolderNode(FolderType.kInboxFolder);
        player.addChild(inbox);
        AgeInfoListNode agesiown = new AgeInfoListNode(FolderType.kAgesIOwnFolder);
        player.addChild(agesiown);

        //create and add relto. We don't need to create AvatarCustomization though.
        NodeUtils.CreateAgeReturnInfo info = NodeUtils.CreateAge(player.getIdx(), "Personal", Guid.fullyRandom(), playerinfo.getIdx(), "Relto", player.getPlayerName()+"'s", player.getPlayerName()+"'s Relto");
            //AgeInfoNode ageinfo = NodeUtils.CreateAgeInfo(age, playerinfo.getIdx(), "Personal", "Relto", player.getPlayerName()+"'s", player.getPlayerName()+"'s Relto");
            //age.addChild(ageinfo);;
        //add relto to the agesiown folder
        AgeLinkNode agelink = NodeUtils.CreateAgeLink(info.ageinfo, "Personal", null);
        agesiown.addChild(agelink);

        //agesiown.addGrandchildren(ageinfo);


        //Chronicles
        //player.addChild(new ChronicleNode(2,"PlayerCensorLevel","1"));
        //player.addChild(new ChronicleNode(2,"PlayerKILevel","0"));
        //player.addChild(new ChronicleNode(2,"KIMarkerLevel","0"));
        //player.addChild(new ChronicleNode(2,"PlayerKIOnlyPMsBuddies","0"));
        //player.addChild(new ChronicleNode(2,"PlayerKIBuddiesOnRequest","0"));
        //player.addChild(new ChronicleNode(1,"MarkerGameData","{'svrGameClientID': -1, 'CGZGameNum': -1, 'isPlayerJoined': 0, 'svrGameTemplateID': '', 'svrGameName': '', 'svrGameTypeID': -1, 'numMarkers': 0, 'numCapturedMarkers': 0, 'svrGameStarted': 0}"));

        //add AgeLink
        /*AgeLinkNode reltonode = CreateAgeLink(playerinfo,"Personal",null);
        player.addChildTree(reltonode);
        AgeLinkNode avacust = CreateAgeLink(playerinfo,"AvatarCustomization","Default:LinkInPointDefault:;");
        player.addChildTree(avacust);
        AgeLinkNode hoodnode = CreateAgeLink(playerinfo,"Neighborhood",null);
        player.addChildTree(hoodnode);
        AgeLinkNode citynode = CreateAgeLink(playerinfo,"city",null);
        player.addChildTree(citynode);

        //add Age and AgeInfo
        {
            AgeNode avacast_age = CreateAge(player,"AvatarCustomization");
            AgeInfoNode avacast_ageinfo = CreateAgeInfo(avacast_age,player,playerinfo,"AvatarCustomization","AvatarCustomization","dtest's","dtest's AvatarCustomization");
            avacast_age.addChild(avacast_ageinfo);
            player.addChildTree(avacast_ageinfo);
        }
        {
            AgeNode avacast_age = CreateAge(player,"Personal");
            AgeInfoNode avacast_ageinfo = CreateAgeInfo(avacast_age,player,playerinfo,"Personal","Relto","dtest's","dtest's Relto");
            avacast_age.addChild(avacast_ageinfo);
            player.addChildTree(avacast_ageinfo);
        }
        {
            AgeNode avacast_age = CreateAge(player,"Neighborhood");
            AgeInfoNode avacast_ageinfo = CreateAgeInfo(avacast_age,player,playerinfo,"Neighborhood","Bevin","DRC","");
            avacast_age.addChild(avacast_ageinfo);
            player.addChildTree(avacast_ageinfo);
        }
        {
            AgeNode avacast_age = CreateAge(player,"city");
            AgeInfoNode avacast_ageinfo = CreateAgeInfo(avacast_age,player,playerinfo,"city","Ae'gura",null,"");
            avacast_age.addChild(avacast_ageinfo);
            player.addChildTree(avacast_ageinfo);
        }*/

        return player;
    }

    public static AgeLinkNode CreateAgeLink(AgeInfoNode ageinfo, String ageFilename, String linkpointInfo)
    {
        AgeLinkNode agelink = new AgeLinkNode(linkpointInfo);

        //old:
        /*//add us
        agelink.addChild(playerinfo);

        //add an empty invites folder
        agelink.addChild(new PlayerInfoListNode(FolderType.kCanVisitFolder));

        //add an owner folder with us in it.
        PlayerInfoListNode owners = new PlayerInfoListNode(FolderType.kAgeOwnersFolder);
            owners.addChild(playerinfo); //make us an owner.
        agelink.addChild(owners);

        //add child ages folder with no child ages in it.
        AgeInfoListNode list = new AgeInfoListNode(FolderType.kChildAgesFolder);
        agelink.addChild(list);

        //add sdl node. todo: add an sdl blob!
        SDLNode sdl = new SDLNode(ageFilename);
        agelink.addChild(sdl);*/

        //new:
        agelink.addChild(ageinfo);

        return agelink;
    }

    public static AgeInfoNode CreateAgeInfo(AgeNode age, /*PlayerNode player,*/ /*PlayerInfoNode playerinfo*/int playerInfoIdx, String ageFilename, String ageInstancename, String ageUserDefinedName, String ageDescription)
    {
        //AgeNode age = CreateAge(player,ageFilename);

        AgeInfoNode ageinfo = new AgeInfoNode(age,ageFilename,ageInstancename,ageUserDefinedName,ageDescription);

        //adds us
        //ageinfo.addChild(playerInfoIdx); //moul has this here too.

        //adds the invite folder
        PlayerInfoListNode canvisitfolder = new PlayerInfoListNode(FolderType.kCanVisitFolder);
        ageinfo.addChild(canvisitfolder);

        //adds the owners folder and adds us to it
        PlayerInfoListNode ownersfolder = new PlayerInfoListNode(FolderType.kAgeOwnersFolder);
            ownersfolder.addChild(playerInfoIdx); //make player an owner.
        ageinfo.addChild(ownersfolder);

        //adds the child ages folder
        AgeInfoListNode childagesfolder = new AgeInfoListNode(FolderType.kChildAgesFolder);
        ageinfo.addChild(childagesfolder);

        //adds the sdl node
        SDLNode sdlnode = new SDLNode(ageFilename,age.n.creatorUuid,age.getIdx()); //is this always okay? Probably not.
        ageinfo.addChild(sdlnode);


        return ageinfo;
    }

    public static class CreateAgeReturnInfo
    {
        AgeNode age;
        AgeInfoNode ageinfo;
    }
    public static CreateAgeReturnInfo CreateAge(int playerId/*PlayerNode player*/, String ageFilename, Guid ageInstanceGuid,      int playerInfoIdx, String ageInstanceName, String ageUserName, String ageDescription)
    {
        //AgeNode age = new AgeNode(playerId/*player*/,ageFilename);
        AgeNode age = new AgeNode(playerId, ageInstanceGuid, ageFilename);

        //Add SystemNode and Global Inbox, which should be shared among all users, but let's just make it per-user for now.
        SystemNode systemnode = GetOrCreateSystemNode();
        age.addChild(systemnode);

        //FolderNode inbox = new FolderNode(FolderType.kInboxFolder);
        //age.addGrandchildren(inbox);

        FolderNode agedevices = new FolderNode(FolderType.kAgeDevicesFolder);
        age.addChild(agedevices);

        AgeInfoListNode subages = new AgeInfoListNode(FolderType.kSubAgesFolder);
        age.addChild(subages);

        FolderNode chronicles = new FolderNode(FolderType.kChronicleFolder);
        age.addChild(chronicles);

        PlayerInfoListNode peopleiknowabout = new PlayerInfoListNode(FolderType.kPeopleIKnowAboutFolder);
        age.addChild(peopleiknowabout);

        //Create the AgeInfoNode
        AgeInfoNode ageinfo = NodeUtils.CreateAgeInfo(age, playerInfoIdx, ageFilename, ageInstanceName, ageUserName, ageDescription);
        age.addChild(ageinfo);

        //AgeInfoListNode agesiown = new AgeInfoListNode(FolderType.kAgesIOwnFolder);
            //AgeLinkNode
        //age.addGrandchildren(agesiown);

        //subscribe to the player's AgesIOwnFolder.
        m.marktime("start");
        Results r = Manager.manager.database.sqlquery("SELECT vault.* FROM vault INNER JOIN ref_vault ON vault.idx=ref_vault.child_idx WHERE parent_idx=? AND vault.type=? AND vault.int_1=?", playerId, NodeType.kNodeAgeInfoList, FolderType.kAgesIOwnFolder);
        m.marktime("end");
        if(r.first())
        {
            //found it!
            AgeInfoListNode agesiown = Node.getNode(r);
            age.addChild(agesiown);
            //Node.(age.getIdx(), agesiown.getIdx(), 0, (byte)0);
        }
        else
        {
            m.throwUncaughtException("hmm... we should have found it.");
        }


        //I'm unsure about these; I haven't looked at Moul's structure.


        //AgeInfoNode ageinfonode = CreateAgeInfo();
        //age.addChild(ageinfonode);


        CreateAgeReturnInfo result = new CreateAgeReturnInfo();
        result.age = age;
        result.ageinfo = ageinfo;
        return result;
    }

    public static String ReportTree(Node node)
    {
        StringBuilder r = new StringBuilder();
        ReportTree(node,r,0);
        return r.toString();
    }
    private static void ReportTree(Node node, StringBuilder r, int indent)
    {
        //report on this node
        for(int i=0;i<indent;i++) r.append("  ");
        r.append(node.toString());
        r.append('\n');

        //report on children
        for(Node child: node.getChildrenNodes())
        {
            ReportTree(child,r,indent+1);
        }
    }

}
