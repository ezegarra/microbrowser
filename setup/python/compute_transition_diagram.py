#
# Computes a transition diagram based on a given log file
#
import csv

# define stages
#
# Search
# Diagram Exploration
# Listing Exploration
stages = {};
stages["unknown"] = 0;
stages["stage_search"] = 0;
stages["stage_diagram_exploration"] = 0;
stages["stage_details_exploration"] = 0;
stages["stage_list_exploration"] = 0;
stages["stage_pattern_usage"] = 0;
stages["stage_answer_usage"] = 0;
stages["stage_setup"] = 0;
stages["stage_knowledge_create"] = 0;
stages["stage_knowledge_view"] = 0;
stages["stage_leaderboard"] = 0;

# define transitions
last_stage = "unknown";
last_event = "";
transitions = {};
for stage in stages:
        transitions[stage] = {};
        for stage2 in stages:
                transitions[stage][stage2] = 0;

print transitions;

# define events
events = {};
events["setup_loaddata"] = 0;
events["setup_loaddata_begin"] = 0;
events["setup_loaddata_end" ] = 0;
events["setup_createactions_begin" ] = 0;
events["setup_createactions_end" ] = 0;
events["search_update"] = 0;
events["discussion_open"] = 0;
events["answer_open"] = 0;
events["pattern_create"] = 0;
events["pattern_open"] = 0;
events["pattern_change_open"] = 0;
events["pattern_change_cancel"] = 0;
events["answer_create"] = 0;
events["answer_create_open"] = 0;
events["edge_create"] = 0;
events["list_mouseentered"] = 0;
events["list_mouseexited"] = 0;
events["list_mousemoved"] = 0;
events["list_pattern_itemclicked"] = 0;
events["list_pattern_mouseentered"] = 0;
events["list_pattern_mousemoved"] = 0;
events["list_pattern_mouseexited"] = 0;
events["diag_itemexited"] = 0;
events["diag_itementered"] = 0;
events["diag_detail_itementered"] = 0;
events["diag_detail_itemexited"] = 0;
events["diag_itemclicked"] = 0;
events["diag_itemdragged"] = 0;
events["diag_detail_itemclicked"] = 0;
events["leaderboard_open"] = 0;
events["leaderboard_close"] = 0;
#events["undefined"] = 0;

total_entries = 0;
# define input file
#inputfile = open('C:\projects\proposal\workspace.microbrowse.0.1\prefuse\doc\\trace_2014-05-07-02-11 - subject01.data', 'r')
#inputfile = open('C:\projects\proposal\workspace.microbrowse.0.1\prefuse\doc\\trace_2014-05-08-11-17 - subject02.data', 'r')
#inputfile = open('C:\projects\proposal\workspace.microbrowse.0.1\prefuse\doc\\trace_2014-05-09-03-28 - subject03.data', 'r')
#inputfile = open('C:\projects\proposal\workspace.microbrowse.0.1\prefuse\doc\\trace_2014-05-14-02-55 - subject04_combined.data', 'r')
#inputfile = open('C:\projects\proposal\workspace.microbrowse.0.1\prefuse\data\usertrace\\S02.txt', 'r')
inputfile = open('C:\projects\proposal\workspace.microbrowse.0.1\prefuse\data\usertrace\\S03.txt', 'r')
# define reader
reader = csv.reader(inputfile)

# process file
for row in reader:
        if len(row) == 0:
                continue;
        elif row[0].startswith( '#' ):
                continue;
        elif not row[0].startswith( '201' ):
                continue;
        
        event = row[1].strip();
        stage = "unknown";
        
        if event in ["search_update"]:
                stage = "stage_search";
        elif event in ["discussion_open"]:
                stage = "stage_knowledge_view";
        elif event in ["pattern_change_open", "pattern_change_cancel", "pattern_create", "pattern_open", "list_pattern_itemclicked"]:
                stage = "stage_pattern_usage";
        elif event in ["answer_create_open"]:
                stage = "stage_knowledge_create"
        elif event in ["list_mouseentered", "list_mouseexited", "list_mousemoved", "list_pattern_mousemoved", "list_pattern_mouseentered", "list_pattern_mouseexited"]:
                stage = "stage_list_exploration";
        elif event in ["diag_itemclicked", "diag_itementered", "diag_itemexited", "diag_itemdragged"]:
                stage = "stage_diagram_exploration";
        elif event in ["edge_create", "setup_loaddata", "answer_create", "setup_createactions_end", "setup_loaddata_begin", "setup_loaddata_end", "setup_createactions_begin", "setup_createactions_end"]:
                stage = "stage_setup";
        elif event in ["leaderboard_open", "leaderboard_close"]:
                stage = "stage_leaderboard";
        elif event in ["diag_detail_itementered", "diag_detail_itemexited", "diag_detail_itemclicked"]:
                stage = "stage_details_exploration";
        elif event in ["answer_open"]:
                stage = "stage_answer_usage";
        else:
                print "** unknown stage event=" + event;

        # update the event count
        events[event] += 1;

        # update the stage count
        stages[stage] += 1;

        # update the transitions count
        transitions[last_stage][stage] += 1;
        
        # update last event entry
        last_event = event;
        last_stage = stage;
        
        # update total entries
        total_entries += 1;

        print row[1];

inputfile.close();

def percentage(part, whole):
  return 100 * float(part)/float(whole)

print "=================================";
print ', '.join(events)
mylist = [];
for event in events:
        #mylist.append(repr(events[event]));
        mylist.append(repr(round(percentage(events[event], total_entries), 2)));
print ', '.join(mylist)
        
for event in events:
        print event + "=" + repr(events[event]) + ", percent=" + repr(round(percentage(events[event], total_entries), 2));

print "=================================";
for stage in stages:
        print stage + "=" + repr(stages[stage]) + ", percent=" + repr(round(percentage(stages[stage], total_entries), 2));

print "===== TRANSITIONS ======";
print "start, end, count";
for transition_start in transitions:
        for transition_end in transitions:
                print transition_start + "," + transition_end + "," + repr(transitions[transition_start][transition_end]);
