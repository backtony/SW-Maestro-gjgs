import React from 'react';
import {
  ScrollView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import {Button} from 'react-native-elements';

interface LectureSearchViewRecentlyKeywordProps {
  setKeyword: (keyword: string) => void;
}

export default class LectureSearchViewRecentlyKeyword extends React.Component<
  LectureSearchViewRecentlyKeywordProps,
  {}
> {
  private recentSearch: string[] = [
    '마카롱 만들기',
    '캔들 만들기',
    '쿠킹 클래스',
  ];
  render() {
    return (
      <View>
        <View style={this.styles.titleWrapper}>
          <Text style={this.styles.title}>최근 검색어</Text>
          <Button
            buttonStyle={{backgroundColor: 'white'}}
            titleStyle={{color: '#8e8e8f', fontSize: 12}}
            onPress={() => {}}
            title={'지우기'}
          />
        </View>
        <ScrollView>
          {this.recentSearch.map(value => (
            <TouchableOpacity
              style={this.styles.button}
              onPress={() => this.props.setKeyword(value)}>
              <Text style={{color: '#1d1d1f', fontSize: 12}}>{value}</Text>
            </TouchableOpacity>
          ))}
        </ScrollView>
      </View>
    );
  }

  private styles = StyleSheet.create({
    titleWrapper: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
      marginTop: 20,
    },
    title: {
      fontSize: 14,
      lineHeight: 16,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#1d1d1f',
    },
    button: {
      paddingVertical: 13,
      borderBottomColor: '#f5f5f7',
      borderBottomWidth: 1,
    },
  });
}
