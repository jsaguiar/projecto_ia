//
//  AdvancedSearchViewController.h
//  ia_project_ios
//
//  Created by Joao Aguiar on 12/17/13.
//  Copyright (c) 2013 Joao Aguiar. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AdvancedSearchViewController : UIViewController
@property (weak, nonatomic) IBOutlet UITextField *fromLabel;
@property (weak, nonatomic) IBOutlet UITextField *SubjectLabel;
@property (weak, nonatomic) IBOutlet UITextField *bodyLabel;
@property (weak, nonatomic) IBOutlet UIDatePicker *fromDatePicker;
@property (weak, nonatomic) IBOutlet UIDatePicker *toDatePicker;
@property (weak, nonatomic) IBOutlet UISwitch *fromdateSwitch;
@property (weak, nonatomic) IBOutlet UISwitch *toDateSwitch;
- (IBAction)touchFromInside:(id)sender;
- (IBAction)touchToInside:(id)sender;

@end
