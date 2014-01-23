//
//  DetailViewController.m
//  ia_project_ios
//
//  Created by Joao Aguiar on 12/17/13.
//  Copyright (c) 2013 Joao Aguiar. All rights reserved.
//

#import "DetailViewController.h"
#import "email.h"
#import "SearchWebView.h"
#import "MBProgressHUD.h"

@interface DetailViewController ()
@property (strong, nonatomic) UIPopoverController *masterPopoverController;
- (void)configureView;
@end

@implementation DetailViewController

#pragma mark - Managing the detail item

- (void)setDetailItem:(id)newDetailItem
{
    if (_detailItem != newDetailItem) {
        _detailItem = newDetailItem;
        
        // Update the view.
        [self configureView];
    }
    
    if (self.masterPopoverController != nil) {
        [self.masterPopoverController dismissPopoverAnimated:YES];
    }
}
- (void) showEmail:(email*)email{
    if (email){
    
        self.fromLabel.text = [email from];
        
        self.toLabel.text=[email to];
        self.subjectLabel.text=[email subject];
        NSString *dateStr = [email dateEmail];
        NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
        [dateFormat setDateFormat:@"yyyyMMddHHmmss"];
        NSDate *date = [dateFormat dateFromString:dateStr];
        
        [dateFormat setDateFormat:@"yyyy/MM/dd HH:mm:ss"];
        NSString *myFormattedDate = [dateFormat stringFromDate:date];
        
        self.dateLabel.text =myFormattedDate;
        //self.dateLabel.text = [email date];
        
        self.detailDescriptionLabel.text = [email description];
        [self formatLabelInTextView:self.subjectLabel];
        if ([[email body]  rangeOfString:@"<p>"].location != NSNotFound || [[email body]  rangeOfString:@"<html>"].location != NSNotFound) {
            [self.webview loadHTMLString:[email body] baseURL:nil];
            //NSLog(@"%d",[self.webview highlightAllOccurencesOfString:@"regards,"]);
            //[self.view bringSubviewToFront:self.webview];
            self.webview.hidden=NO;
            self.bodyTextField.hidden=YES;
            [self.view bringSubviewToFront:self.btnRecommendation];
        } else{
            self.bodyTextField.text=[email body];
            [self.bodyTextField setEditable:FALSE];
            [self formatTextInTextView:self.bodyTextField];
            //[self.view bringSubviewToFront:self.bodyTextField];
            //[self.view bringSubviewToFront:self.btnRecommendation];
            self.webview.hidden=YES;
            self.bodyTextField.hidden=NO;
        }
        
        
        /*dispatch_async(dispatch_get_main_queue(), ^{
            [self buildRecommendationView];
        });*/
    }
     // [self.view bringSubviewToFront:self.recommendationView];

    
}

- (void)configureView
{
    // Update the user interface for the detail item.
    
    if (self.detailItem) {
        if (!self.recommendationHidden){
            [self hideRecommendationView:YES];
        }
        dispatch_async(dispatch_get_main_queue(), ^{
            [MBProgressHUD showHUDAddedTo:self.view animated:YES];
        });
        self.bodyTextField.text=@"";
        [self.webview loadHTMLString:@"" baseURL:nil];
        
        [self showEmail:self.detailItem];

        [self scoringFuncion];
    }
    

}


-(void) buildRecommendationView{
    if (self.recommendationHidden){
        NSLog(@"hide it");
       /* CGRect aux= self.recommendationView.frame;
        aux.origin.x+=self.recommendationView.frame.size.width;
        [self.recommendationView setFrame:aux];*/
        //[self hideRecommendationView:NO];
    }
    else{
        
        /*CGRect aux= self.recommendationView.frame;
        aux.origin.x-=self.recommendationView.frame.size.width;
        [self.recommendationView setFrame:aux];*/
        NSLog(@"show it");
       //[self showRecommendationView:NO];

    }
    [self.view bringSubviewToFront:self.recommendationView];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
	// Do any additional setup after loading the view, typically from a nib.
    [self configureView];
    self.recommendationHidden=YES;
    self.recommendationView.translucentAlpha = 1;
    self.recommendationView.translucentStyle = UIBarStyleBlack;
    self.recommendationView.translucentTintColor = [UIColor clearColor];
    self.recommendationView.backgroundColor = [UIColor clearColor];
    
    [self.recommendationTable setBackgroundColor:[UIColor clearColor]];

    /*UISwipeGestureRecognizer *swipeLe = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(showRecommendationView)];
     swipeLe.direction=UISwipeGestureRecognizerDirectionLeft;
     [self.webview addGestureRecognizer:swipeLe];
     [self.bodyTextField addGestureRecognizer:swipeLe];*/
    
    
    /*  UISwipeGestureRecognizer *swipeLe = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(hideSearchView)];
     swipeLe.direction=UISwipeGestureRecognizerDirectionLeft;
     [self.searchView addGestureRecognizer:swipeLe];*/
    
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
/*
 -(NSString *) stringByStrippingHTML {
 NSRange r;
 NSString *s = [[self copy] autorelease];
 while ((r = [s rangeOfString:@"<[^>]+>" options:NSRegularExpressionSearch]).location != NSNotFound)
 s = [s stringByReplacingCharactersInRange:r withString:@""];
 return s;
 }*/

- (void)formatTextInTextView:(UITextView *)textView
{
    //textView.scrollEnabled = NO;
    // NSRange selectedRange = textView.selectedRange;
    NSString *text = textView.text;
    
    // This will give me an attributedString with the base text-style
    NSMutableAttributedString *attributedString;
    
    attributedString = [[NSMutableAttributedString alloc] initWithString:text];
    
    NSRange range = NSMakeRange(0,[text length]);
    
    
    [attributedString addAttribute:NSFontAttributeName
                             value:[UIFont systemFontOfSize:16.0]
                             range: range];
    
    
    NSError *error = nil;
    NSString *regular=@"(";
    
    NSArray *split = [self.search componentsSeparatedByString:@" "];
    for (NSString* word in split) {
        if ([regular compare:@"("]==0) {
            regular=[NSString stringWithFormat:@"%@%@",regular,word];
        }
        regular=[NSString stringWithFormat:@"%@|%@",regular,word];
    }
    regular=[NSString stringWithFormat:@"%@)",regular];
    
    
    
    
    NSRegularExpression *regex = [NSRegularExpression regularExpressionWithPattern:regular options:NSRegularExpressionCaseInsensitive error:&error];
    NSArray *matches = [regex matchesInString:text
                                      options:0
                                        range:NSMakeRange(0, text.length)];
    
    for (NSTextCheckingResult *match in matches)
    {
        NSRange matchRange = [match rangeAtIndex:0];
        [attributedString addAttribute:NSBackgroundColorAttributeName
                                 value:[UIColor yellowColor]
                                 range:matchRange];
    }
    
    textView.attributedText = attributedString;
    //textView.selectedRange = selectedRange;
    //textView.scrollEnabled = YES;
}



- (void)formatLabelInTextView:(UILabel *)label
{
    NSString *text = label.text;
    
    // This will give me an attributedString with the base text-style
    NSMutableAttributedString *attributedString = [[NSMutableAttributedString alloc] initWithString:text];
    
    NSRange range = NSMakeRange(0,[text length]);
    
    
    [attributedString addAttribute:NSFontAttributeName
                             value:[UIFont systemFontOfSize:16.0]
                             range: range];
    
    
    NSError *error = nil;
    NSString *regular=@"(";
    
    NSArray *split = [self.search componentsSeparatedByString:@" "];
    for (NSString* word in split) {
        if ([regular compare:@"("]==0) {
            regular=[NSString stringWithFormat:@"%@%@",regular,word];
        }
        regular=[NSString stringWithFormat:@"%@|%@",regular,word];
    }
    regular=[NSString stringWithFormat:@"%@)",regular];
    
    
    NSRegularExpression *regex = [NSRegularExpression regularExpressionWithPattern:regular options:0 error:&error];
    NSArray *matches = [regex matchesInString:text
                                      options:0
                                        range:NSMakeRange(0, text.length)];
    
    for (NSTextCheckingResult *match in matches)
    {
        NSRange matchRange = [match rangeAtIndex:0];
        [attributedString addAttribute:NSBackgroundColorAttributeName
                                 value:[UIColor yellowColor]
                                 range:matchRange];
    }
    
    label.attributedText = attributedString;
}


- (IBAction)touchRecommendation:(id)sender {
    if (self.recommendationHidden) {
        [self showRecommendationView:YES];
        
        
    }else{
        [self hideRecommendationView:YES];
        
    }
}

- (void) hideRecommendationView:(BOOL)animated{
    if (animated) {
        [UIView beginAnimations:nil context:NULL];
        [UIView setAnimationDuration:0.5];
        
    }
    
    CGRect aux=self.recommendationView.frame;
    aux.origin.x+=self.recommendationView.frame.size.width;
    [self.recommendationView setFrame:aux];
    
    aux=self.bodyTextField.frame;
    
    aux.size.width+=self.recommendationView.frame.size.width;
    [self.bodyTextField setFrame:aux];
    
    aux=self.webview.frame;
    aux.size.width+=self.recommendationView.frame.size.width;
    [self.webview setFrame:aux];
    
    aux=self.btnRecommendation.frame;
    aux.origin.x +=self.recommendationView.frame.size.width;
    [self.btnRecommendation setFrame:aux];
    [self.btnRecommendation setSelected:NO];

    if (animated) {
        self.recommendationHidden=YES;
        [UIView commitAnimations];
    }
    
}

- (void) showRecommendationView:(BOOL)animated{
    [self.btnRecommendation setSelected:YES];

    dispatch_async(dispatch_get_main_queue(), ^{

    if (animated) {
        [UIView beginAnimations:nil context:NULL];
        [UIView setAnimationDuration:0.5];
    }

    CGRect aux=self.recommendationView.frame;
    aux.origin.x-=self.recommendationView.frame.size.width;
    [self.recommendationView setFrame:aux];
    
    
    aux=self.bodyTextField.frame;
    aux.size.width-=self.recommendationView.frame.size.width;
    [self.bodyTextField setFrame:aux];
    
    aux=self.webview.frame;
    aux.size.width-=self.recommendationView.frame.size.width;
    [self.webview setFrame:aux];
    
    
    aux=self.btnRecommendation.frame;
    aux.origin.x-=self.recommendationView.frame.size.width;
    [self.btnRecommendation setFrame:aux];
    if (animated) {
        self.recommendationHidden=NO;
        [UIView commitAnimations];
    }
    });
    
}

#pragma mark - Split view

- (void)splitViewController:(UISplitViewController *)splitController willHideViewController:(UIViewController *)viewController withBarButtonItem:(UIBarButtonItem *)barButtonItem forPopoverController:(UIPopoverController *)popoverController
{
    barButtonItem.title = NSLocalizedString(@"Master", @"Master");
    [self.navigationItem setLeftBarButtonItem:barButtonItem animated:YES];
    self.masterPopoverController = popoverController;
}

- (void)splitViewController:(UISplitViewController *)splitController willShowViewController:(UIViewController *)viewController invalidatingBarButtonItem:(UIBarButtonItem *)barButtonItem
{
    // Called when the view is shown again in the split view, invalidating the button and popover controller.
    [self.navigationItem setLeftBarButtonItem:nil animated:YES];
    self.masterPopoverController = nil;
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 15;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"RecCell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    email *aux;
    if (indexPath.row==0) {
        aux=(email*)self.detailItem;
        [cell setBackgroundColor:[UIColor blackColor]];
    }
    else{
       aux=(email*)[self.allMails objectAtIndex:indexPath.row-1];
        [cell setBackgroundColor:[UIColor clearColor]];

    }
    cell.textLabel.text=aux.subject;
    cell.detailTextLabel.text=aux.body;
    
    
    [cell.textLabel setTextColor:[UIColor whiteColor]];
    
    [cell.detailTextLabel setTextColor:[UIColor whiteColor]];
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    dispatch_async(dispatch_get_main_queue(), ^{
        if (indexPath.row==0){
            [self showEmail:(email*)self.detailItem];
        }else{
            [self showEmail:(email*) [self.allMails objectAtIndex:indexPath.row-1]];
        }
    });
}

#pragma mark - scoring

-(void) scoringFuncion{
    email *chosenMail=(email*)self.detailItem;
    NSArray *cat=chosenMail.categories;
    for (email* mail in self.allMails) {
        int currentScore=0;
        if ( chosenMail.identifier==mail.identifier) continue;
        for (int i=0; i<mail.categories.count; i++) {
            for (int j=0; j<cat.count; j++) {
                if ([(NSString*) mail.categories[i] integerValue] ==[(NSString*) cat[j] integerValue]){
                    currentScore+=1;
                }
            }
        }
        mail.score=[NSNumber numberWithInt:currentScore];
    }
    
    NSSortDescriptor *highestToLowest = [NSSortDescriptor sortDescriptorWithKey:@"score" ascending:NO];
    [self.allMails sortUsingDescriptors:[NSArray arrayWithObject:highestToLowest]];
    
    [self.recommendationTable reloadData];
    
    
    dispatch_async(dispatch_get_main_queue(), ^{
        [MBProgressHUD hideHUDForView:self.view animated:YES];
    });
    

}

@end
