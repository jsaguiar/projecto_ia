//
//  AdvancedSearchViewController.m
//  ia_project_ios
//
//  Created by Joao Aguiar on 12/17/13.
//  Copyright (c) 2013 Joao Aguiar. All rights reserved.
//

#import "AdvancedSearchViewController.h"
#import "MasterViewController.h"
@interface AdvancedSearchViewController ()

@end

@implementation AdvancedSearchViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    UIBarButtonItem *addButton = [[UIBarButtonItem alloc]
                                  initWithBarButtonSystemItem:UIBarButtonSystemItemDone target:self action:@selector(advancedSearch)];
    
    self.navigationItem.rightBarButtonItem = addButton;
    [self.fromDatePicker setHidden:TRUE];
    [self.toDatePicker setHidden:TRUE];



}
-(void) viewWillAppear:(BOOL)animated{
    [self.fromDatePicker setHidden:TRUE];
    [self.toDatePicker setHidden:TRUE];

}

- (void)didReceiveMemoryWarning
{
    
    [super didReceiveMemoryWarning];
    
  
    // Dispose of any resources that can be recreated.
}
-(void)advancedSearch{
    
    NSString *toDate=@"";
    NSString *fromDate=@"";
    NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
    [dateFormat setDateFormat:@"yyyyMMdd000000"];
    NSDate *date;
    if (self.fromdateSwitch.isOn){
        date=self.fromDatePicker.date;
        fromDate = [dateFormat stringFromDate:date];
    }else{
        fromDate=@"00000000000000";
    }
    [dateFormat setDateFormat:@"yyyyMMdd999999"];

    if (self.toDateSwitch.isOn){
        date=self.toDatePicker.date;
        toDate = [dateFormat stringFromDate:date];
    }else{
        date=[[NSDate alloc]init];
        toDate = [dateFormat stringFromDate:date];
    }
  
    
    
    MasterViewController *rootViewController = ((MasterViewController *)self.navigationController.viewControllers[0]);
    [rootViewController advancedSearchWithFrom:self.fromLabel.text withSubject:self.SubjectLabel.text withBody:self.bodyLabel.text withFromDate:fromDate wihtToDate:toDate ];
    
    
    [[self navigationController] popViewControllerAnimated:YES]; 
   // [[NSNotificationCenter defaultCenter] postNotificationName:@"advancedSearch" object:nil];

}

- (IBAction)touchFromInside:(id)sender {
    if( self.fromdateSwitch.isOn){
        [UIView beginAnimations:nil context:NULL];
        [UIView setAnimationDuration:1];
        [self.fromDatePicker setHidden:FALSE];
        [UIView commitAnimations];
    }else{
        [UIView beginAnimations:nil context:NULL];
        [UIView setAnimationDuration:1];
        [self.fromDatePicker setHidden:TRUE];
        [UIView commitAnimations];
    }
}

- (IBAction)touchToInside:(id)sender {
    if( self.toDateSwitch.isOn){
        [UIView beginAnimations:nil context:NULL];
        [UIView setAnimationDuration:2];
        [self.toDatePicker setHidden:FALSE];
        [UIView commitAnimations];
    }else{
        [UIView beginAnimations:nil context:NULL];
        [UIView setAnimationDuration:2];
        [self.toDatePicker setHidden:TRUE];
        [UIView commitAnimations];
    }

}
@end
