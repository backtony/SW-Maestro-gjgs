import Category from '@/utils/Category';
import React from 'react';
import {
  ImageBackground,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';

interface MatchingCategoryViewBlockProps {
  mainCategory: string;
  selectedMainCategory: string;
  setMainCategory: (main: string) => void;
}

export default class MatchingCategoryViewBlock extends React.Component<
  MatchingCategoryViewBlockProps,
  {}
> {
  render() {
    return (
      <TouchableOpacity
        key={this.props.mainCategory}
        style={this.styles.button}
        onPress={() => {
          this.props.setMainCategory(this.props.mainCategory);
          //   this.setState({selectedMainCategory: this.props.mainCategory});
        }}>
        <ImageBackground
          source={{
            uri: Category.getUnsplashImageUrl(this.props.mainCategory),
          }}
          style={this.styles.img}
          imageStyle={{borderRadius: 10}}>
          <View
            style={[
              this.styles.textWrapper,
              this.props.selectedMainCategory === this.props.mainCategory
                ? {backgroundColor: 'rgba(79, 108, 255, 0.8)'}
                : {},
            ]}>
            <Text style={this.styles.text}>{this.props.mainCategory}</Text>
          </View>
        </ImageBackground>
      </TouchableOpacity>
    );
  }

  private styles = StyleSheet.create({
    button: {
      width: '45%',
      marginBottom: 50,
    },
    img: {
      width: 152,
      height: 152,
      alignItems: 'center',
      justifyContent: 'center',
    },
    textWrapper: {
      width: '100%',
      height: '100%',
      backgroundColor: 'rgba(0,0,0, 0.2)',
      borderRadius: 10,
      alignItems: 'center',
      justifyContent: 'center',
    },
    text: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 24,
      color: '#FFF',
    },
  });
}
