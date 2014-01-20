//
//  MasterViewController.h
//  ia_project_ios
//
//  Created by Joao Aguiar on 12/17/13.
//  Copyright (c) 2013 Joao Aguiar. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ServerManager.h"


@class DetailViewController;

@interface MasterViewController : UITableViewController <ServerManagerDelegate, UISearchDisplayDelegate, UISearchBarDelegate>{
    NSArray *_groups;
}
- (IBAction)clearSearch:(id)sender;

-(void) advancedSearchWithFrom:(NSString*) from withSubject:(NSString*) subject withBody:(NSString*) body withFromDate:(NSString*) fromdate wihtToDate: (NSString*) toDate;

@property (weak, nonatomic) IBOutlet UISearchBar *simpleSearch;
@property (strong, nonatomic) DetailViewController *detailViewController;
@property ServerManager *manager;
@property (strong, nonatomic) NSArray *emails;

@end
