import Category from '@/utils/Category';
import {numberJsonType} from '@/utils/Types';
import Zone from '@/utils/Zone';
import React from 'react';
import {View, ScrollView, Text} from 'react-native';
import RightScrollViewCategoryButton from './RightScrollViewCategoryButton';
import CategoryTitle from './CategoryTitle';
import ZoneButton from './ZoneButton';
import ZoneTitle from './ZoneTitle';
import {StyleSheet} from 'react-native';

interface RightScrollViewProps {
  navigation: any;
  yCord: numberJsonType;
  setYCord: (yCord: numberJsonType) => void;
  setRef: (ref: ScrollView | null) => void;
}

export default class RightScrollView extends React.Component<
  RightScrollViewProps,
  {}
> {
  private keyCount: number = 0;
  render() {
    return (
      <ScrollView
        style={this.rightScrollViewStyle.scrollView}
        ref={ref => this.props.setRef(ref as ScrollView | null)}
        showsVerticalScrollIndicator={false}
        showsHorizontalScrollIndicator={false}>
        <ZoneTitle
          yCord={this.props.yCord}
          setYCord={(yCord: numberJsonType) => {
            this.props.setYCord(yCord);
          }}
        />
        {Zone.getZoneList().map(main => (
          <View key={this.keyCount++}>
            <Text style={this.rightScrollViewStyle.text}>{main}</Text>
            <View style={this.rightScrollViewStyle.buttonContainer}>
              {Zone.getSubZoneList(main).map(sub => (
                <ZoneButton
                  subCategory={sub}
                  navigation={this.props.navigation}
                />
              ))}
            </View>
          </View>
        ))}

        {Category.getCategoryList().map(main => (
          <View
            onLayout={event => {
              const layout = event.nativeEvent.layout;
              let yCord = this.props.yCord;
              yCord[main] = layout.y;
              this.props.setYCord(yCord);
            }}>
            <CategoryTitle mainCategory={main} />
            <View style={this.rightScrollViewStyle.buttonContainer}>
              {Category.getSubCategoryList(main).map(sub => (
                <RightScrollViewCategoryButton
                  navigation={this.props.navigation}
                  subCategory={sub}
                />
              ))}
            </View>
          </View>
        ))}
        <View style={this.rightScrollViewStyle.bottomBlock} />
      </ScrollView>
    );
  }

  private rightScrollViewStyle = StyleSheet.create({
    scrollView: {width: 300, backgroundColor: '#FFF'},
    text: {
      fontSize: 14,
      fontWeight: 'bold',
      color: '#1d1d1f',
      marginTop: 6,
      marginLeft: 10,
    },
    buttonContainer: {
      flexDirection: 'row',
      marginLeft: 5,
      marginVertical: 5,
      flexWrap: 'wrap',
    },
    bottomBlock: {height: 500},
  });
}
