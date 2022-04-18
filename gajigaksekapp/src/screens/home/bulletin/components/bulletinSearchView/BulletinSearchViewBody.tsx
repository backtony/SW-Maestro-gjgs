import Category from '@/utils/Category';
import React from 'react';
import {
  ScrollView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import {Button} from 'react-native-elements';

export default class BulletinSearchViewBody extends React.Component {
  private recentSearch: string[] = [
    '마카롱 만들기',
    '캔들 만들기',
    '쿠킹 클래스',
  ];
  render() {
    return (
      <View style={{padding: 20}}>
        <Text style={this.styles.title1}>인기 검색어</Text>
        <ScrollView
          horizontal={true}
          showsVerticalScrollIndicator={false}
          showsHorizontalScrollIndicator={false}>
          {Category.getSubCategoryList('액티비티').map(item => (
            <TouchableOpacity
              style={this.styles.button1}
              onPress={() => {
                this.setState({keyword: item.sub});
              }}>
              <Text style={this.styles.buttonText1}>{item.sub}</Text>
            </TouchableOpacity>
          ))}
        </ScrollView>
        <View style={this.styles.wrapper}>
          <Text style={this.styles.text2}>최근 검색어</Text>
          <Button
            buttonStyle={{backgroundColor: 'white'}}
            titleStyle={{color: '#8e8e8f', fontSize: 12}}
            onPress={() => {}}
            title={'지우기'}
          />
        </View>
        <ScrollView
          showsVerticalScrollIndicator={false}
          showsHorizontalScrollIndicator={false}>
          {this.recentSearch.map(value => (
            <TouchableOpacity
              style={this.styles.button2}
              onPress={() => this.setState({keyword: value})}>
              <Text style={{color: '#1d1d1f', fontSize: 12}}>{value}</Text>
            </TouchableOpacity>
          ))}
        </ScrollView>
      </View>
    );
  }

  private styles = StyleSheet.create({
    title1: {
      fontSize: 14,
      lineHeight: 16,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 20,
      marginBottom: 10,
      color: '#1d1d1f',
    },
    button1: {
      backgroundColor: '#f5f7ff',
      padding: 10,
      paddingBottom: 7,
      borderRadius: 6,
      flexDirection: 'row',
      marginRight: 10,
      alignItems: 'center',
      justifyContent: 'center',
      borderColor: '#4f6cff',
      borderWidth: 1,
    },
    buttonText1: {
      fontFamily: 'NotoSansCJKkr-Regular',
      fontSize: 12,
      lineHeight: 14,
      color: '#4f6cff',
    },
    wrapper: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
      marginTop: 20,
    },
    text2: {
      fontSize: 14,
      lineHeight: 16,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#1d1d1f',
    },
    button2: {
      paddingVertical: 13,
      borderBottomColor: '#f5f5f7',
      borderBottomWidth: 1,
    },
  });
}
