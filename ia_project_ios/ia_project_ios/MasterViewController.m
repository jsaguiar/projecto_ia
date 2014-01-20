//
//  MasterViewController.m
//  ia_project_ios
//
//  Created by Joao Aguiar on 12/17/13.
//  Copyright (c) 2013 Joao Aguiar. All rights reserved.
//

#import "MasterViewController.h"

#import "DetailViewController.h"
#import "ServerCommunicator.h"
#import "email.h"


@interface MasterViewController () {
    NSMutableArray *_objects;
}
@end

@implementation MasterViewController

- (void)awakeFromNib
{
    self.clearsSelectionOnViewWillAppear = NO;
    self.preferredContentSize = CGSizeMake(320.0, 600.0);
    [super awakeFromNib];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
        /*self.navigationItem.leftBarButtonItem = self.editButtonItem;

    UIBarButtonItem *addButton = [[UIBarButtonItem alloc]
                                  initWithBarButtonSystemItem:UIBarButtonSystemItemSearch target:self action:@selector(advancedSearch)];
    
    self.navigationItem.leftBarButtonItem = addButton;*/
    
    self.detailViewController = (DetailViewController *)[[self.splitViewController.viewControllers lastObject] topViewController];
    
    self.manager = [[ServerManager alloc] init];
    self.manager.communicator = [[ServerCommunicator alloc] init];
    self.manager.communicator.delegate = _manager;
    self.manager.delegate = self;
    
    [self.manager fetchMails];
    
    //[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(advancedSearch) name:@"advancedSearch" object:nil];


    
}


-(void) didReceiveMails:(NSArray *)groups  {
    self.emails=groups;
    dispatch_async(dispatch_get_main_queue(), ^{
        [self.tableView reloadData];
        if(_emails.count>0){
            email *object = _emails[0];
            self.detailViewController.detailItem = object;
        }
       
    });
    
}

-(void) advancedSearchWithFrom:(NSString*) from withSubject:(NSString*) subject withBody:(NSString*) body withFromDate:(NSString*) fromdate wihtToDate: (NSString*) toDate {
    NSLog(@"Subject advancedSearchWithFrom = %@",subject);
    [_manager fetchComplexSearchWithFrom:from withSubject:subject withBody:body withFromDate:fromdate wihtToDate:toDate];
}

- (void)startFetchingGroups:(NSNotification *)notification
{
    [_manager fetchMails];
}

- (void)fetchingGroupsFailedWithError:(NSError *)error
{
    NSLog(@"Error %@; %@", error, [error localizedDescription]);
  
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)insertNewObject:(id)sender
{
    if (!_objects) {
        _objects = [[NSMutableArray alloc] init];
    }
    [_objects insertObject:[NSDate date] atIndex:0];
    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:0 inSection:0];
    [self.tableView insertRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationAutomatic];
}

#pragma mark - Table View

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.emails.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"mailscell" forIndexPath:indexPath];

    email *email = self.emails[indexPath.row];
    
    UILabel *subjectLabel = (UILabel *)[cell viewWithTag:1];
    subjectLabel.text = email.subject;
    
    UILabel *bodyLabel = (UILabel *)[cell viewWithTag:2];
    bodyLabel.text = email.body;
    cell.backgroundColor=[UIColor whiteColor];
    
    if ([email.polarity integerValue]>0){
        cell.backgroundColor=[UIColor greenColor];
    }else if([email.polarity integerValue]<0){
        cell.backgroundColor=[UIColor redColor];
    }
    
    return cell;
}

- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the specified item to be editable.
    return YES;
}

/*- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        [_objects removeObjectAtIndex:indexPath.row];
        [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
    } else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view.
    }
}*/

/*
// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
{
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    email *object = _emails[indexPath.row];
    
    self.detailViewController.detailItem = object;
}


- (void)updateFilteredContentForProductName:(NSString *)productName type:(NSString *)typeName{
    NSLog(@"%@",productName);
    NSLog(@"%@",typeName);

}
- (void)didReceiveSimpleSearch: (NSArray *)groups{
    NSLog(@"didReceiveSimpleSearch");
}


- (BOOL)searchDisplayController:(UISearchDisplayController *)controller shouldReloadTableForSearchString:(NSString *)searchString
{
    NSLog(@"%@",searchString);
    
    //NSString *scope;
    
    //[self updateFilteredContentForProductName:searchString type:scope];
    
    // Return YES to cause the search result table view to be reloaded.
    return NO;
}
-(void)searchBarSearchButtonClicked:(UISearchBar *)searchBar{
    [self.manager fetchSimpleSearch:searchBar.text];
    self.detailViewController.search=searchBar.text;

    [searchBar resignFirstResponder];


    
}
- (void)searchBarTextDidBeginEditing:(UISearchBar *)searchBar{
}


- (void)searchBarTextDidEndEditing:(UISearchBar *)searchBar{
}
-(BOOL)searchBarShouldEndEditing:(UISearchBar *)searchBar{
    return YES;
}

- (IBAction)clearSearch:(id)sender {
    self.simpleSearch.text=@"";
    self.detailViewController.search=@"";
    [_manager fetchMails];
    [self.simpleSearch resignFirstResponder];
}
@end
