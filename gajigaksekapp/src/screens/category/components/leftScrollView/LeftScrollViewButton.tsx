import {numberJsonType} from '@/utils/Types';
import React from 'react';
import {
  Image,
  Text,
  TouchableOpacity,
  ScrollView,
  StyleSheet,
} from 'react-native';

interface CategoryButtonProps {
  mainCategory: string;
  rightScrollView: ScrollView | null;
  buttonInfo: any;
  yCord: numberJsonType;
  setMainCategory: (mainCategory: string) => void;
}

export default class LeftScrollViewButton extends React.Component<
  CategoryButtonProps,
  {}
> {
  render() {
    return (
      <TouchableOpacity
        key={this.props.buttonInfo.title}
        style={[
          this.CategoryButtonStyle.button,
          this.props.mainCategory === this.props.buttonInfo.title
            ? this.CategoryButtonStyle.selected
            : this.CategoryButtonStyle.unselected,
        ]}
        onPress={() => {
          if (this.props.rightScrollView) {
            this.props.rightScrollView.scrollTo({
              y: this.props.yCord[this.props.buttonInfo.title],
            });
          }
          this.props.setMainCategory(this.props.buttonInfo.title);
        }}>
        <Image
          style={{
            width: this.props.buttonInfo.width,
            height: this.props.buttonInfo.height,
          }}
          source={this.props.buttonInfo.imageSource}
        />
        <Text
          style={[
            this.CategoryButtonStyle.text,
            this.props.mainCategory === this.props.buttonInfo.title
              ? this.CategoryButtonStyle.selectedText
              : this.CategoryButtonStyle.unselectedText,
          ]}>
          {this.props.buttonInfo.title}
        </Text>
      </TouchableOpacity>
    );
  }
  private CategoryButtonStyle = StyleSheet.create({
    button: {
      width: 60,
      height: 60,
      backgroundColor: '#FFF',
      alignItems: 'center',
      justifyContent: 'center',
      borderTopRightRadius: 30,
      borderBottomRightRadius: 30,
      marginBottom: 10,
    },
    selected: {
      backgroundColor: '#f5f7ff',
    },
    unselected: {},
    text: {
      fontSize: 10,
      fontWeight: 'bold',
      color: '#4a4a4c',
      marginTop: 6,
    },
    selectedText: {color: '#5564b0'},
    unselectedText: {},
  });
}
