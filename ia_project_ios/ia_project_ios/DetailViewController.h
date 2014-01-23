//
//  DetailViewController.h
//  ia_project_ios
//
//  Created by Joao Aguiar on 12/17/13.
//  Copyright (c) 2013 Joao Aguiar. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "email.h"
#import "ILTranslucentView.h"
@interface DetailViewController : UIViewController <UISplitViewControllerDelegate,UIGestureRecognizerDelegate,UITableViewDataSource,UITableViewDelegate>

@property (strong, nonatomic) id detailItem;
@property (strong,nonatomic) NSString *search;
@property BOOL recommendationHidden;
@property (weak, nonatomic) IBOutlet UILabel *detailDescriptionLabel;
@property (weak, nonatomic) IBOutlet UILabel *fromLabel;
@property (weak, nonatomic) IBOutlet UILabel *toLabel;
@property (weak, nonatomic) IBOutlet UILabel *subjectLabel;
@property (weak, nonatomic) IBOutlet UITextView *bodyTextField;
@property (weak, nonatomic) IBOutlet UIWebView *webview;
@property (weak, nonatomic) IBOutlet UILabel *dateLabel;
@property (weak, nonatomic) IBOutlet UIButton *btnRecommendation;
@property (weak, nonatomic) IBOutlet ILTranslucentView *recommendationView;
@property (weak, nonatomic) IBOutlet UITableView *recommendationTable;
@property (strong,nonatomic) NSMutableArray *allMails;

@end
